//Start na página
$(window).ready(function(){
    $("#loader-img").hide();
    $("#fim-btn").hide();
});


//Infinite Scroll
var pageNumber = 0;

//Verifica onde se encontra a barra de rolagem do navegador
$(window).scroll(function(){
    var scrollTop = $(this).scrollTop();
    var conteudo = $(document).height() - $(window).height();

    //console.log("scrollTop: ", scrollTop, "|| ", "conteudo: ", conteudo);
    //console.log("Window: ", $(window).height());
    //console.log("Document: ", $(document).height());
    if(scrollTop >= conteudo){
        console.log("HORA DE FAZER A NOVA REQUISIÇÃO.")
        pageNumber++;
        setTimeout(function(){
            loadByScrollBar(pageNumber);
        }, 200); //Delay de 200 ms
    }
});

//Faz a requisição para mais promoções através de paginação

function loadByScrollBar(pageNumber){
    var site = $("#autocomplete-input").val();
    $.ajax({
        method: "GET",
        url: "/promocao/list/ajax",
        data: {
            page: pageNumber,
            site: site
        },
        beforeSend: function(){
            $("#loader-img").show();
        },
        success: function(response){
            //Vem um html completamente preenchido pelo o thymeleaf
            console.log("Resposta do servidor: ", response);
            console.log("TAMANHO DA RESPOSTA: ", response.length);
            if(response.length > 150){
                $(".row").fadeIn(250, function(){
                   $(this).append(response);
                });
            }else{
                $("#fim-btn").show();
                $("#loader-img").removeClass("loader");
            }
        }
    });
}

//Curtidas
//Por causa dos Cards adicionados posteriormente ao carregamento do DOM, ao procurar pelo os botões procura-se
//No Documente que onde contêm tudo mesmo os que foram adicionados posteriormente.
$(document).on("click", "button[id*='likes-btn-']", function(){
    //Vai ouvir todos os botões que têm essa composição no nome do id.
    var id = $(this).attr("id").split("-")[2];
    console.log("ID: ", id);
    $.  ajax({
        method: "POST",
        url: "/promocao/like/" + id,
        success: function(response){
            $("#likes-count-"+id).text(response);
        },
        error: function(xhr){
            alert("Ops.. algum erro aconteceu. " + xhr.statusText + xhr.status);
        }
    });
});


//AutoComplete
$("#autocomplete-input").autocomplete({
    source: function(request, response){
        $.ajax({
            method: "GET",
            url: "/promocao/site",
            data: {
                termo: request.term
            },
            success: function(result){
                response(result);
            }
        })
    },
});

//Submit busca por site
$("#autocomplete-submit").on("click", function(){
    var site = $("#autocomplete-input").val();
    $.ajax({
        method: "GET",
        url: "/promocao/site/list",
        data: {
            site: site
        },
        beforeSend: function(){
            $("#fim-btn").hide();
            pageNumber = 0; //Como a variável já pode ter sido incrementada é necessário zerar.
            $(".row").fadeOut(400, function(){
                $(this).empty();
            });
        },
        success: function(response){
            $(".row").fadeIn(250, function(){
                $(this).append(response);
            });
        },
        error: function(xhr){
            alert("Ops... Tivemos algum problema. ", xhr.statusText, xhr.status);
        }
    });

});
