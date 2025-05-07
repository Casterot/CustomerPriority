document.addEventListener('DOMContentLoaded', function() {
    // La variable nombreDuplicadoErrorGlobal se espera que sea definida
    // en el HTML usando Thymeleaf antes de que este script se cargue.
    if (typeof nombreDuplicadoErrorGlobal !== 'undefined' && nombreDuplicadoErrorGlobal) {
        // Llama a la función global definida en permisos.js (que usa mostrarMensajeTemporal)
        // Asegúrate de que mostrarMensajeTemporal esté disponible globalmente o importa/define aquí.
        if (typeof mostrarMensajeTemporal === 'function') {
            mostrarMensajeTemporal('Ya existe un rol con este nombre.', 'danger');
        } else {
            console.error('La función mostrarMensajeTemporal no está definida.');
            // Fallback por si la función no está disponible (opcional)
            alert('Ya existe un rol con este nombre.');
        }

        // Opcional: Enfocar el campo de nombre de rol para facilitar la corrección
        var nombreRolInput = document.getElementById('nombreRol');
        if (nombreRolInput) {
            nombreRolInput.focus();
            nombreRolInput.select(); // Seleccionar el texto existente
        }
    }
});
