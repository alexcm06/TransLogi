document.addEventListener("DOMContentLoaded", () => {
    const modalEliminar = document.getElementById("modalEliminarViaje");
    if (modalEliminar) {
        modalEliminar.addEventListener("show.bs.modal", function (event) {
            const boton = event.relatedTarget;
            document.getElementById("idViajeEliminar").value =
                    boton.dataset.id;
            document.getElementById("nombreViaje").textContent =
                    boton.dataset.nombre;
        });
    }
});
