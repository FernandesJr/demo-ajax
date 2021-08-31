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
                $("#alert").removeClass("alert alert-danger").text("");
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