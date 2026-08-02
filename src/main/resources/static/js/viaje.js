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

document.addEventListener("DOMContentLoaded", () => {

    const buscador = document.getElementById("buscarViaje");
    const filtroEstado = document.getElementById("filtroEstado");
    const filas = document.querySelectorAll("#tablaViajes tbody tr");

    function filtrar() {

        const texto = buscador.value.toLowerCase().trim();
        const estadoSeleccionado = filtroEstado.value.toLowerCase();

        filas.forEach(fila => {

            if (fila.querySelector("td[colspan]")) {
                return;
            }

            const contenido = fila.textContent.toLowerCase();

            const estado = fila.querySelector(".estado")
                    .textContent
                    .trim()
                    .toLowerCase();

            const coincideTexto = contenido.includes(texto);

            const coincideEstado =
                    estadoSeleccionado === "" ||
                    estado === estadoSeleccionado;

            fila.style.display =
                    (coincideTexto && coincideEstado)
                    ? ""
                    : "none";

        });

    }

    buscador.addEventListener("input", filtrar);
    filtroEstado.addEventListener("change", filtrar);

});
