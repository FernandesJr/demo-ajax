//JS responsável por capturar o input da url de promoção e fazer a requisição Ajax

$("#linkPromocao").on("change", function(){
    //Ao modificar qualquer coisa no Input de Url irá cair aqui
    var url = $(this).val();

    // http://t.me no min 11 caracteres para uma requisição
    if(url.length >= 11){
        $.ajax({
            method: "POST",
            url: "/meta/info?url=" + url,
            cache: false,
            beforeSend: function(){
                $("#alert").removeClass("alert alert-danger alert-success").text("");
                $("#titulo").val("");
                $("#linkImagem").attr("src", "");
                $("#site").text("");
                $("#loader-img").addClass("loader");
            },
            success: function(result){
                //console.log(result);
                $("#titulo").val(result.title);
                $("#linkImagem").attr("src", result.imagem);
                $("#site").text(result.site.replace("@","")); //Caso a requisição venha através da TwitterCard retiramos o @
            },
            statusCode: {
                404: function(){
                    $("#alert").addClass("alert alert-danger").text("Nenhuma informação pôde ser recuperada dessa url.");
                    $("#linkImagem").attr("src", "/images/promo-dark.png");
                }
            },
            error: function(){
                $("#alert").addClass("alert alert-danger").text("Ops... url inválida.");
                $("#linkImagem").attr("src", "/images/promo-dark.png");
            },
            complete: function(){
                $("#loader-img").removeClass("loader")
            }
        })
    }
})

//Submeter o formulário de cadastro de promoção via Ajax
$("#form-add-promo").submit(function(evt){
    //Desabilitando o evento natural do submit do formulário, ele não vai atualizar a página depois de submeter
    evt.preventDefault();

    //Json a ser enviado para o servidor
    var promo = {};
    promo.linkPromocao = $("#linkPromocao").val();
    promo.titulo = $("#titulo").val();
    promo.descricao = $("#descricao").val();
    promo.linkImagem = $("#linkImagem").attr("src");
    promo.site = $("#site").text();
    promo.preco = $("#preco").val();
    promo.categoria = $("#categoria").val();

    console.log("> promo: ", promo);

    $.ajax({
        method: "POST",
        url: "/promocao/save",
        data: promo,
        beforeSend: function(){
            $("#form-add-promo").hide(); //Esconde o form
            $("#loader-form").addClass("loader").show();

            //Retirando avisos de validação
            $("span").closest(".error-span").remove(); //Retira a classe de todas as Tag <span> da página

            //Removendo as bordas danger das validações
            $("#linkPromocao").removeClass("is-invalid");
            $("#titulo").removeClass("is-invalid");
            $("#preco").removeClass("is-invalid");
            $("#categoria").removeClass("is-invalid");
            
            //Poderia substituir toda essa sequência de um a um por;
            //$(".is-invalid").removeClass("is-invalid");
        },
        success: function(){
            //Esse each percorre todos os campos do form, os que são input ele reseta.
            $("#form-add-promo").each(function(){
                this.reset();
            });
            $("#linkImagem").attr("src", "/images/promo-dark.png");
            $("#site").text("");
            $("#alert")
                .removeClass("alert alert-danger")
                .addClass("alert alert-success")
                .text("OK! Promoção cadastrada com sucesso.");
        },
        statusCode: {
            422: function(xhr){
                 console.log("> status error: " + xhr.status);
                 var errors = $.parseJSON(xhr.responseText); //Convertendo o Json em uma variável
                 $.each(errors, function(key, val){
                    $("#"+key).addClass("is-invalid");
                    $("#error-"+key).addClass("invalid-feedback").append("<span class='error-span'>"+ val + "</span>");
                 })
            },
        },
        error: function(xhr){
            $("#alert").addClass("alert alert-danger").text("Falha ao cadastrar essa promoção.");
            console.log("> error: ", xhr.responseText);
        },
        complete: function(){
            $("#loader-form").fadeOut(800, function(){
                //Fechando o loader em 800 ms e paralelamente executando a função
                $("#form-add-promo").fadeIn(250); //Trazendo o form de volta a tela
                $("#loader-form").removeClass("loader");
            });
        }
    });
});