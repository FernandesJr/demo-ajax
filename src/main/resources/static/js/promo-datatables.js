$(document).ready(function(){
//Após carregar os elementos da página
    moment.locale("pt-br"); //https://momentjs.com/
    var table = $("#table-server").DataTable({
        processing: true,
        responsive: true,
        serverSide: true,
        lengthMenu: [10, 15, 20, 25],
        ajax: {
            url: "/promocao/datatables/server",
            data: "data"
        },
        columns: [
            {data: "id"},
            {data: "titulo"},
            {data: "site"},
            {data: "linkPromocao"},
            {data: "descricao"},
            {data: "linkImagem"},
            {data: "preco", render: $.fn.dataTable.render.number(".",",",2,"R$ ")},
            {data: "likes"},
            {data: "dataCadastro", render:
                function(dataCadastro){
                    return moment(dataCadastro).format('LLLL');
                }
            },
            {data: "categoria.titulo"},
        ],
        dom: 'Bfrtip',
        buttons: [
            {
                text: "editar",
                attr: {
                    id: "btn-edit-table",
                    type: "button",
                },
                enabled: false,
            },
            {
                text: "excluir",
                attr: {
                    id: "btn-del-table",
                    type: "button",
                },
                enabled: false,
            }
        ]
    });

    //Selecionando uma linha na tabela
    $("#table-server tbody").on("click", "tr", function(){
        if($(this).hasClass("selected")){
            $(this).removeClass("selected");
            table.buttons().disable();
        }else{
            $("tr.selected").removeClass("selected");
            $(this).addClass("selected");
            table.buttons().enable();
        }
    });

    //Desabilitar botões ao clicar no thead
    $("#table-server thead").on("click", "tr", function(){
        table.buttons().disable();
    });

    //Ação do botão EDITAR
    $("#btn-edit-table").on("click", function(){
        if(isSelectedRow()){
            var id = getPromoId();
            $.ajax({
                method: "GET",
                url: "/promocao/edit/" + id,
                beforeSend: function(){
                    //Retirando avisos de validação
                    $("span").closest(".error-span").remove(); //Retira a classe de todas as Tag <span> da página

                    //Removendo as bordas danger das validações
                    $(".is-invalid").removeClass("is-invalid");
                },
                success: function(data){
                    $("#edt_id").val(data.id);
                    $("#edt_titulo").val(data.titulo);
                    $("#edt_preco").val(data.preco.toLocaleString("pt-BR",{
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    }));
                    $("#edt_categoria").val(data.categoria.id);
                    $("#edt_descricao").val(data.descricao);
                    $("#edt_linkImagem").val(data.linkImagem);
                    $("#edt_imagem").attr("src", data.linkImagem);
                    $("#edt_site").text(data.site);

                    $("#modal-form").modal("show");
                },
                error: function(){
                    alert("Ops.. tivemos um problema inesperado.")
                }
            });
        }
    });

    //Ação do botão EXCLUIR
    $("#btn-del-table").on("click", function(){
        if(isSelectedRow()){
            var id = getPromoId();
            $("#modal-delete").modal("show");
        }
    });

    function getPromoId(){
        return table.row(table.$("tr.selected")).data().id;
    }

    function isSelectedRow(){
        var trow = table.row(table.$("tr.selected"));
        return trow.data() !== undefined;
    }

    //Excluir promoção
    $("#btn-del-modal").on("click", function(){
        var id = getPromoId();
        $.ajax({
            method: "GET",
            url: "/promocao/deletar/" + id,
            success: function(){
                $("#modal-delete").modal("hide");
                table.ajax.reload();
            },
            error: function(){
                alert("Ops... Aconteceu algum imprevisto, tente mais tarde.");
            }
        });
    });

    //Edição
    $("#btn-edit-modal").on("click", function(){
        //Json a ser enviado para o servidor
        var promo = {};
        promo.titulo = $("#edt_titulo").val();
        promo.descricao = $("#edt_descricao").val();
        promo.linkImagem = $("#edt_linkImagem").val();
        promo.preco = $("#edt_preco").val();
        promo.categoria = $("#edt_categoria").val();
        promo.id = $("#edt_id").val();

        $.ajax({
            method: "POST",
            url: "/promocao/edit",
            data: promo,
            beforeSend: function(){
                //Retirando avisos de validação
                $("span").closest(".error-span").remove(); //Retira a classe de todas as Tag <span> da página

                //Removendo as bordas danger das validações
                $(".is-invalid").removeClass("is-invalid");
            },
            success: function(){
                $("#modal-form").modal("hide");
                table.ajax.reload(null, false); //Os parâmetros servem para ao recarregar permanecer na mesma página
            },
            statusCode: {
                422: function(xhr){
                    console.log("> status error: " + xhr.status);
                    var errors = $.parseJSON(xhr.responseText); //Convertendo o Json em uma variável
                    $.each(errors, function(key, val){
                        $("#edt_"+key).addClass("is-invalid");
                        $("#error-"+key).addClass("invalid-feedback").append("<span class='error-span'>"+ val + "</span>");
                    })
                },
            }
        });
    });

    //Modificando a img no modal de edição
    $("#edt_linkImagem").on("change", function(){
        var link = $("#edt_linkImagem").val();
        $("#edt_imagem").attr("src", link);
    });

    // ação para desabilitar botoes ao clicar na paginação
    $('#table-server_paginate').on('click', 'a', function(){
        if(!$(this).hasClass('current') && !$(this).hasClass('disabled')) {
            table.buttons().disable();
        }
    });

    // ação para desabilitar botoes ao clicar na search
    $("#table-server_filter").on("click", "input", function(){
        table.buttons().disable();
        $("tr.selected").removeClass("selected");
    });

});

