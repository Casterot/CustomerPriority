// Script para confirmar y mostrar mensajes al restablecer contraseña

document.addEventListener('DOMContentLoaded', function () {
  // Delegación de eventos para todos los formularios de restablecer contraseña
  document.querySelectorAll('form[action*="/usuarios/restablecer-contrasena/"]').forEach(function(form) {
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      const confirmed = window.confirm('¿Está seguro que desea restablecer la contraseña a su valor por defecto?');
      if (confirmed) {
        // Enviar el formulario (restablecer contraseña)
        fetch(form.action, {
          method: 'POST',
          headers: {
            'X-Requested-With': 'XMLHttpRequest',
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: new URLSearchParams(new FormData(form))
        })
        .then(response => {
          if (response.ok) {
            mostrarMensaje('La contraseña ha sido restablecida correctamente.', 'success');
          } else {
            mostrarMensaje('Error al restablecer la contraseña.', 'danger');
          }
        })
        .catch(() => mostrarMensaje('Error de red al restablecer la contraseña.', 'danger'));
      }
    });
  });
});

// Función para mostrar mensajes flotantes
function mostrarMensaje(mensaje, tipo = 'success') {
  let alert = document.createElement('div');
  alert.className = `alert alert-${tipo} alert-dismissible fade show position-fixed top-0 end-0 m-4`;
  alert.role = 'alert';
  alert.style.zIndex = 2000;
  alert.innerHTML = `${mensaje}<button type='button' class='btn-close' data-bs-dismiss='alert'></button>`;
  document.body.appendChild(alert);
  setTimeout(() => {
    alert.classList.remove('show');
    alert.classList.add('hide');
    setTimeout(() => alert.remove(), 500);
  }, 3500);
}
