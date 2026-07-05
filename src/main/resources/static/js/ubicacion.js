document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("modalEliminarUbicacion");
    if (!modal) return;
    modal.addEventListener("show.bs.modal", event => {
        const boton = event.relatedTarget;
        document.getElementById("idUbicacionEliminar").value =
            boton.dataset.id;
        document.getElementById("nombreUbicacion").textContent =
            boton.dataset.nombre;
    });

});