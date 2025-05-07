const hamBurger = document.querySelector("#toggle-btn");

hamBurger.addEventListener("click", function () {
  document.querySelector("#sidebar").classList.toggle("expand");
  document.querySelector(".main").classList.toggle("expanded");
  document.querySelector(".navbar").classList.toggle("expanded");
});

// Función para mostrar mensaje temporal
function mostrarMensajeTemporal(mensaje, tipo = 'success', duracion = 3000) {
  // Eliminar mensaje anterior si existe
  const mensajeAnterior = document.querySelector('.mensaje-temporal');
  if (mensajeAnterior) {
    mensajeAnterior.remove();
  }

  // Crear el mensaje
  const mensajeElement = document.createElement('div');
  mensajeElement.className = `alert alert-${tipo} mensaje-temporal`;
  mensajeElement.style.position = 'fixed';
  mensajeElement.style.top = '20px';
  mensajeElement.style.left = '50%';
  mensajeElement.style.transform = 'translateX(-50%)';
  mensajeElement.style.zIndex = '1050'; // Asegurar que esté sobre otros elementos
  mensajeElement.style.padding = '10px 20px';
  mensajeElement.style.borderRadius = '5px';
  mensajeElement.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
  mensajeElement.textContent = mensaje;

  // Añadir al body
  document.body.appendChild(mensajeElement);

  // Eliminar después de la duración especificada
  setTimeout(() => {
    mensajeElement.remove();
  }, duracion);
}

// Inicializar tooltips de Bootstrap
document.addEventListener('DOMContentLoaded', function () {
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl)
  })
});

