// Función para manejar la asignación de permisos

document.addEventListener('DOMContentLoaded', function() {
    const filtroInput = document.getElementById('filtroPermisos');
    const container = document.querySelector('.permissions-container');

    // Asegurarse de que ambos elementos existen antes de añadir el listener
    if (filtroInput && container) {
        const permisos = container.querySelectorAll('.form-check'); // Selecciona todos los checkboxes

        filtroInput.addEventListener('input', function() {
            const textoBusqueda = this.value.toLowerCase().trim();

            permisos.forEach(function(permisoDiv) {
                const label = permisoDiv.querySelector('.form-check-label');
                // Busca en el texto del nombre (strong) y la descripción (small)
                const textoLabel = label ? label.textContent.toLowerCase() : '';

                if (textoLabel.includes(textoBusqueda)) {
                    permisoDiv.style.display = ''; // Muestra el elemento si coincide
                } else {
                    permisoDiv.style.display = 'none'; // Oculta el elemento si no coincide
                }
            });
        });
    } else {
         // Opcional: Mensaje en consola si no se encuentran los elementos esperados
         // Descomentar las siguientes líneas para depuración si es necesario
         // if (!filtroInput) console.log("Elemento #filtroPermisos no encontrado.");
         // if (!container) console.log("Elemento .permissions-container no encontrado.");
    }
});

// --- Fin del código para filtrar permisos ---
function actualizarPermisosRol(rolId) {
    const permisosSeleccionados = [];
    document.querySelectorAll('input[name="permisos"]:checked').forEach(checkbox => {
        permisosSeleccionados.push(parseInt(checkbox.value));
    });

    fetch(`/roles/${rolId}/permisos/guardar`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify({ permisosIds: permisosSeleccionados })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al guardar los permisos');
        }
        return response.json();
    })
    .then(data => {
        mostrarMensajeExito('Permisos actualizados correctamente');
        setTimeout(() => {
            window.location.reload();
        }, 1500);
    })
    .catch(error => {
        mostrarMensajeError('Error al actualizar los permisos: ' + error.message);
    });
}

// Función para mostrar mensaje de éxito
function mostrarMensajeExito(mensaje) {
    Swal.fire({
        title: '¡Éxito!',
        text: mensaje,
        icon: 'success',
        confirmButtonText: 'Aceptar'
    });
}

// Función para mostrar mensaje de error
function mostrarMensajeError(mensaje) {
    Swal.fire({
        title: '¡Error!',
        text: mensaje,
        icon: 'error',
        confirmButtonText: 'Aceptar'
    });
}