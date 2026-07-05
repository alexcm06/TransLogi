document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("modalEliminarEmpresa");
    if (!modal) return;
    modal.addEventListener("show.bs.modal", event => {
        const boton = event.relatedTarget;
        document.getElementById("idEmpresaEliminar").value =
            boton.dataset.id;
        document.getElementById("nombreEmpresa").textContent =
            boton.dataset.nombre;
    });

});