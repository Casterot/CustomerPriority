document.addEventListener('DOMContentLoaded', function() {
    const btnAgregarRol = document.getElementById('btnAgregarRol');
    const selectRol = document.getElementById('rolId');
    const rolesAsignados = document.getElementById('rolesAsignados');
    const mensajeSinRoles = document.getElementById('mensajeSinRoles');
    const btnGuardarCambios = document.getElementById('btnGuardarCambios');
    const usuarioId = document.getElementById('usuarioId').value;
    
    // Obtener el token CSRF
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    // Conjunto para almacenar los IDs de roles ya agregados
    const rolesAgregados = new Set();
    
    // Inicializar roles existentes
    document.querySelectorAll('.role-item').forEach(item => {
        const rolId = item.getAttribute('data-rol-id');
        if (rolId) {
            rolesAgregados.add(rolId);
        }
    });
    
    // Función para agregar un rol a la lista
    function agregarRol(rolId, nombreRol) {
        if (rolesAgregados.has(rolId)) {
            alert('Este rol ya ha sido agregado');
            return;
        }
        
        rolesAgregados.add(rolId);
        
        // Ocultar mensaje de "No hay roles asignados" si existe
        if (mensajeSinRoles) {
            mensajeSinRoles.style.display = 'none';
        }
        
        // Crear elemento para el nuevo rol
        const nuevoRol = document.createElement('div');
        nuevoRol.className = 'role-item';
        nuevoRol.setAttribute('data-rol-id', rolId);
        
        nuevoRol.innerHTML = `
            <span>${nombreRol}</span>
            <button type="button" class="btn btn-danger btn-sm btn-eliminar-rol">
                <i class="bi bi-trash"></i>
            </button>
        `;
        
        // Agregar evento para eliminar el rol
        nuevoRol.querySelector('.btn-eliminar-rol').addEventListener('click', function() {
            rolesAgregados.delete(rolId);
            nuevoRol.remove();
            
            // Mostrar mensaje si no hay roles
            if (rolesAsignados.querySelectorAll('.role-item').length === 0) {
                if (mensajeSinRoles) {
                    mensajeSinRoles.style.display = 'block';
                }
            }
        });
        
        rolesAsignados.appendChild(nuevoRol);
    }
    
    // Evento para agregar rol
    if (btnAgregarRol) {
        btnAgregarRol.addEventListener('click', function() {
            const rolId = selectRol.value;
            if (!rolId) {
                alert('Por favor seleccione un rol');
                return;
            }
            
            const nombreRol = selectRol.options[selectRol.selectedIndex].text;
            
            // Verificar si el rol ya está en la lista
            if (rolesAgregados.has(rolId)) {
                alert('Este rol ya ha sido agregado');
                return;
            }
            
            agregarRol(rolId, nombreRol);
            selectRol.value = ''; // Resetear selección
        });
    }
    
    // Evento para guardar cambios
    if (btnGuardarCambios) {
        btnGuardarCambios.addEventListener('click', function() {
            const rolesIds = Array.from(rolesAgregados);
            
            // Mostrar indicador de carga
            btnGuardarCambios.disabled = true;
            btnGuardarCambios.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Guardando...';
            
            // Enviar solicitud para guardar los roles
            fetch(`/usuarios/${usuarioId}/roles/guardar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ rolesIds: rolesIds })
            })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/usuarios';
                } else {
                    return response.text().then(text => {
                        throw new Error(text || 'Error al guardar los roles');
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al guardar los roles: ' + error.message);
                // Restaurar botón
                btnGuardarCambios.disabled = false;
                btnGuardarCambios.innerHTML = '<i class="bi bi-check2-circle me-2"></i>Guardar Cambios';
            });
        });
    }
    
    // Agregar eventos para eliminar roles existentes
    document.querySelectorAll('.btn-eliminar-rol').forEach(btn => {
        btn.addEventListener('click', function() {
            const rolItem = this.closest('.role-item');
            const rolId = rolItem.getAttribute('data-rol-id');
            rolesAgregados.delete(rolId);
            rolItem.remove();
            
            // Mostrar mensaje si no hay roles
            if (rolesAsignados.querySelectorAll('.role-item').length === 0) {
                if (mensajeSinRoles) {
                    mensajeSinRoles.style.display = 'block';
                }
            }
        });
    });
});