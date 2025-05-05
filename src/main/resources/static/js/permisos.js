// Función para manejar la asignación de permisos
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