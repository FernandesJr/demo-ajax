$(document).ready(function(){
//Após carregar os elementos da página
    $("#table-server").DataTable({
        processing: true,
        responsive: true,
        serverSide: true,
        lengthMenu: [10, 15, 20, 25],
        ajax{
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
            {data: "preco"},
            {data: "likes"},
            {data: "dataCadastro"},
            {data: "categoria.titulo"},
        ]
    });

});