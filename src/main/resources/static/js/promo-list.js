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
    $.ajax({
        method: "GET",
        url: "/promocao/list/ajax",
        data: {
            page: pageNumber
        },
        beforeSend: function(){
            $("#loader-img").show();
        },
        success: function(response){
            //Vem um html completamente prenchido pelo o thymeleaf
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
