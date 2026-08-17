document.addEventListener("DOMContentLoaded", () => {
    // Carga los datos del conductor seleccionado en el modal de borrado.
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
