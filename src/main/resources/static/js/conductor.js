document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("modalEliminarConductor");
    if (!modal) return;
    modal.addEventListener("show.bs.modal", event => {
        const boton = event.relatedTarget;
        document.getElementById("idConductorEliminar").value =
            boton.dataset.id;
        document.getElementById("nombreConductor").textContent =
            boton.dataset.nombre;
    });

});