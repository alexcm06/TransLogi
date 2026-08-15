const modalEliminar=document.getElementById('modalEliminarUsuario');

modalEliminar.addEventListener('show.bs.modal',function(event){

    const button=event.relatedTarget;

    document.getElementById("idUsuarioEliminar").value=
            button.getAttribute("data-id");

    document.getElementById("nombreUsuarioEliminar").innerHTML=
            button.getAttribute("data-nombre");

});