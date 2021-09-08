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
            $("#modal-form").modal("show");
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

});

