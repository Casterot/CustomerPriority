document.addEventListener("DOMContentLoaded", function () {
  const tipoDocumento = document.getElementById("tipoDocumento");
  const documento = document.getElementById("documento");
  const correo = document.getElementById("correo");
  const telefono = document.getElementById("telefono");
  const fechaNacimiento = document.getElementById("fechaNacimiento");

  const apellidoPaterno = document.getElementById("apellidoPaterno");
  const apellidoMaterno = document.getElementById("apellidoMaterno");
  const nombreCompleto = document.getElementById("nombreCompleto");
  const direccion = document.getElementById("direccion");
  const genero = document.getElementById("genero");
  const validarDNIBtn = document.getElementById("validarDNI");
  const tipoContrato = document.getElementById("tipoContrato");
  const departamento = document.getElementById("departamento");
  const provincia = document.getElementById("provincia");
  const distrito = document.getElementById("distrito");
  const idTrabajador = document.querySelector('input[name="idTrabajador"]');
  const rucInput = document.getElementById('rucTrabajador');
  const estadoInput = document.getElementById('estadoTrabajador');
  const validarRucButton = document.getElementById('validarRUC');
  const rucErrorMensaje = document.getElementById('rucErrorMensaje');

  // Determinar si estamos en modo edición (cuando idTrabajador tiene un valor)
  const esEdicion = idTrabajador && idTrabajador.value !== "" && idTrabajador.value !== "0";

  // Elementos para navegación entre pestañas
  const siguienteContactoBtn = document.getElementById('siguiente-contacto');
  const siguienteLaboralBtn = document.getElementById('siguiente-laboral');
  const anteriorDatosPersonalesBtn = document.getElementById('anterior-datos-personales');
  const anteriorContactoBtn = document.getElementById('anterior-contacto');

  // API Keys y tokens
  const API_KEY_DNI = 'cGVydWRldnMucHJvZHVjdGlvbi5maXRjb2RlcnMuNjdiYTk3Y2U5ZmE0MTczZjYxMzIwN2Vk';
  const API_TOKEN_CEE = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzODI2OSIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6ImNvbnN1bHRvciJ9.qDE0B-scA7FippoTnBgt3Q-TFRGn8xt0oD5gFwQpE8g';

  // Variable para controlar si el documento ha sido validado
  let documentoValidado = false;
  let originalPlaceholders = {};

  // Función para limpiar campos RUC y Estado
  function limpiarCamposRucEstado() {
    if (rucInput) rucInput.value = '';
    if (estadoInput) estadoInput.value = '';
    if (rucErrorMensaje) {
      rucErrorMensaje.textContent = '';
      rucErrorMensaje.style.display = 'none';
    }
  }

  // Función para bloquear/desbloquear campos de datos personales
  function toggleCamposPersonales(bloquear) {
    const campos = [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento, genero];
    campos.forEach(campo => {
      if (campo) {
        campo.readOnly = bloquear;
        campo.disabled = false;
        if (bloquear) {
          campo.classList.add('bg-light');
          campo.style.backgroundColor = '#e9ecef';
        } else {
          campo.classList.remove('bg-light');
          campo.style.backgroundColor = '';
        }
      }
    });
  }

  // Función para bloquear todos los campos de datos personales incluyendo el tipo de documento y documento
  function bloquearCamposDatosPersonales() {
    const camposPersonales = [tipoDocumento, documento, apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento, genero];

    camposPersonales.forEach(campo => {
      if (campo) {
        campo.readOnly = true;
        campo.disabled = false;
        campo.classList.add('bg-light');
        campo.style.backgroundColor = '#e9ecef';
        campo.style.userSelect = 'none';
        campo.style.pointerEvents = 'none';
      }
    });

    // Ocultar el botón de validación
    if (validarDNIBtn) {
      validarDNIBtn.style.display = 'none';
    }
  }

  // Función para desbloquear campos que no son de datos personales
  function desbloquearCamposEdicion() {
    // Desbloquear campos de contacto
    if (correo) {
      correo.readOnly = false;
      correo.disabled = false;
      correo.classList.remove('bg-light');
      correo.style.backgroundColor = '';
    }

    if (telefono) {
      telefono.readOnly = false;
      telefono.disabled = false;
      telefono.classList.remove('bg-light');
      telefono.style.backgroundColor = '';
    }

    if (direccion) {
      direccion.readOnly = false;
      direccion.disabled = false;
      direccion.classList.remove('bg-light');
      direccion.style.backgroundColor = '';
    }

    // Desbloquear campos de ubicación
    if (departamento) {
      departamento.disabled = false;
      departamento.classList.remove('bg-light');
      departamento.style.backgroundColor = '';
    }

    if (provincia) {
      provincia.disabled = false;
      provincia.classList.remove('bg-light');
      provincia.style.backgroundColor = '';
    }

    if (distrito) {
      distrito.disabled = false;
      distrito.classList.remove('bg-light');
      distrito.style.backgroundColor = '';
    }

    // Desbloquear campos laborales
    if (tipoContrato) {
      tipoContrato.disabled = false;
      tipoContrato.classList.remove('bg-light');
      tipoContrato.style.backgroundColor = '';
    }

  }

  // Bloquear campos inicialmente
  toggleCamposPersonales(true);

  // Seleccionar RxH como tipo de contrato por defecto
  if (tipoContrato) {
    const opciones = tipoContrato.options;
    for (let i = 0; i < opciones.length; i++) {
      if (opciones[i].textContent === 'RxH') {
        tipoContrato.selectedIndex = i;
        break;
      }
    }
  }

  // Si estamos en modo edición, bloquear campos de datos personales y desbloquear los demás
  if (esEdicion) {
    // Bloquear campos de datos personales
    bloquearCamposDatosPersonales();

    // Desbloquear campos que sí se pueden editar
    desbloquearCamposEdicion();

    // Marcar el documento como validado para evitar validaciones adicionales
    documentoValidado = true;
  }

  // Función para mostrar mensaje de error
  function mostrarError(elemento, mensaje) {
    // Eliminar mensaje de error anterior si existe
    const errorAnterior = elemento.parentElement.querySelector('.invalid-feedback');
    if (errorAnterior) {
      errorAnterior.remove();
    }

    // Eliminar clase de error si existe
    elemento.classList.remove('is-invalid');
    elemento.classList.remove('is-valid');

    // Crear y mostrar nuevo mensaje de error
    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback';
    feedback.textContent = mensaje;
    elemento.parentElement.appendChild(feedback);
    elemento.classList.add('is-invalid');
  }

  // Función para mostrar check de validación
  function mostrarValidacion(elemento) {
    // Eliminar mensaje de error anterior si existe
    const errorAnterior = elemento.parentElement.querySelector('.invalid-feedback');
    if (errorAnterior) {
      errorAnterior.remove();
    }

    // Eliminar clase de error si existe
    elemento.classList.remove('is-invalid');

    // Añadir clase de validación
    elemento.classList.add('is-valid');
  }

  // Función genérica para actualizar el estado de cualquier botón de validación
  function actualizarEstadoBoton(boton, estado, textoOriginal = 'Validar', ancho = '140px') {
    if (!boton) return;

    // Eliminar clases de colores y spinner previos
    boton.classList.remove('btn-secondary', 'btn-success', 'btn-primary', 'btn-danger');
    // No eliminar spinner-border directamente aquí porque está en el innerHTML

    // Guardar el texto original si no existe
    if (!boton.getAttribute('data-original-text')) {
      boton.setAttribute('data-original-text', textoOriginal);
    } else {
      textoOriginal = boton.getAttribute('data-original-text');
    }

    // Establecer ancho fijo
    boton.style.width = ancho;
    boton.style.minWidth = ancho;
    boton.style.maxWidth = ancho;
    boton.style.textAlign = 'center';
    boton.style.whiteSpace = 'nowrap';
    boton.style.overflow = 'visible';
    boton.style.textOverflow = 'clip';
    boton.style.padding = '0.375rem 0.75rem';

    switch (estado) {
      case 'default':
        boton.classList.add('btn-secondary');
        boton.innerHTML = textoOriginal;
        boton.disabled = false;
        break;
      case 'loading':
        boton.classList.add('btn-primary');
        boton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Cargando...';
        boton.disabled = true;
        break;
      case 'success':
        boton.classList.add('btn-success');
        boton.innerHTML = '<i class="bi bi-check-circle-fill me-1"></i> Validado';
        boton.disabled = true;
        break;
      case 'error':
        boton.classList.add('btn-danger');
        boton.innerHTML = '<i class="bi bi-x-circle-fill me-1"></i> Error';
        boton.disabled = false;
        break;
    }
  }

  // Refactorización: función específica para el botón de DNI
  function actualizarEstadoBotonValidacion(estado) {
    actualizarEstadoBoton(validarDNIBtn, estado, 'Validar', '140px');
  }

  // Nueva función para el botón de RUC
  function actualizarEstadoBotonValidacionRUC(estado) {
    actualizarEstadoBoton(validarRucButton, estado, 'Validar RUC', '120px');
  }

  // Función para consultar la API de RUC
  async function consultarRUC(ruc) {
    // Limpiar siempre antes de iniciar la validación
    if (rucErrorMensaje) {
      rucErrorMensaje.textContent = '';
      rucErrorMensaje.style.display = 'none';
    }
    try {
      actualizarEstadoBotonValidacionRUC('loading');
      // Llamada a la API de validación de RUC
      const response = await fetch(`https://api.perudevs.com/api/v1/ruc/complete?document=${ruc}&key=${API_KEY_DNI}`);
      const data = await response.json();
      if (data.estado && data.resultado) {
        if (estadoInput) {
          estadoInput.value = data.resultado.estado || '';
          estadoInput.readOnly = true;
          estadoInput.style.backgroundColor = '#e8e8e8';
          estadoInput.style.userSelect = 'none';
          estadoInput.style.pointerEvents = 'none';
          estadoInput.style.border = '1px solid #dee2e6';
          estadoInput.classList.remove('is-valid');
        }
        actualizarEstadoBotonValidacionRUC('success');
        // No mostrar mensaje de error
        if (rucErrorMensaje) {
          rucErrorMensaje.textContent = '';
          rucErrorMensaje.style.display = 'none';
        }
      } else {
        if (estadoInput) {
          estadoInput.value = '';
          estadoInput.readOnly = true;
          estadoInput.style.backgroundColor = '#e8e8e8';
          estadoInput.style.userSelect = 'none';
          estadoInput.style.pointerEvents = 'none';
          estadoInput.style.border = '1px solid #dee2e6';
        }
        actualizarEstadoBotonValidacionRUC('default'); // No mostrar error en el botón
        if (rucErrorMensaje) {
          rucErrorMensaje.textContent = data.mensaje || 'RUC no válido o no encontrado.';
          rucErrorMensaje.style.display = 'block';
        }
      }
    } catch (error) {
      actualizarEstadoBotonValidacionRUC('default'); // No mostrar error en el botón
      // Solo mostrar mensaje si no hay ya uno visible
      if (rucErrorMensaje && (!rucErrorMensaje.textContent || rucErrorMensaje.textContent === '')) {
        rucErrorMensaje.textContent = 'Error al validar el RUC. Intente nuevamente.';
        rucErrorMensaje.style.display = 'block';
      }
    }
  }

  // EventListener para el botón Validar RUC
  if (validarRucButton) {
    validarRucButton.addEventListener('click', function () {
      const ruc = rucInput.value.trim();
      if (ruc.length === 11 && /^[0-9]+$/.test(ruc)) {
        consultarRUC(ruc);
      } else {
        actualizarEstadoBotonValidacionRUC('error');
        if (rucErrorMensaje) {
          rucErrorMensaje.textContent = 'Ingrese un RUC válido de 11 dígitos.';
          rucErrorMensaje.style.display = 'block';
        }
      }
    });
  }

  // Asegura que el botón Validar RUC se reinicie a default cuando se limpie el campo RUC
  if (rucInput) {
    rucInput.addEventListener('input', function () {
      actualizarEstadoBotonValidacionRUC('default');
      if (rucErrorMensaje) {
        rucErrorMensaje.textContent = '';
        rucErrorMensaje.style.display = 'none';
      }
    });
  }

  // Función para reiniciar los campos de datos personales
  function reiniciarCamposPersonales() {
    // Limpiar los campos
    apellidoPaterno.value = '';
    apellidoMaterno.value = '';
    nombreCompleto.value = '';
    fechaNacimiento.value = '';
    genero.value = '';

    // Desbloquear los campos
    apellidoPaterno.readOnly = false;
    apellidoMaterno.readOnly = false;
    nombreCompleto.readOnly = false;
    fechaNacimiento.readOnly = false;
    genero.style.pointerEvents = '';
    genero.style.backgroundColor = '';

    // Eliminar indicadores visuales de validación
    [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento, genero, documento].forEach(campo => {
      campo.classList.remove('is-valid');
      campo.classList.remove('is-invalid');
      campo.classList.remove('bg-light');
      campo.classList.remove('bg-secondary');
      campo.classList.remove('text-white');
      campo.style.backgroundColor = '';
      campo.style.userSelect = '';
      campo.style.pointerEvents = '';

      // Eliminar mensajes de feedback
      const feedback = campo.parentElement.querySelector('.valid-feedback');
      if (feedback) {
        feedback.remove();
      }
      const errorFeedback = campo.parentElement.querySelector('.invalid-feedback');
      if (errorFeedback) {
        errorFeedback.remove();
      }
    });

    // Eliminar mensaje de éxito si existe
    const mensajeExito = document.querySelector('.alert-success');
    if (mensajeExito) {
      mensajeExito.remove();
    }

    // Reiniciar el estado de validación del DNI
    documentoValidado = false;

    // Reiniciar el estado del botón
    actualizarEstadoBotonValidacion('default');
  }

  // Función para validar campo requerido
  function validarCampoRequerido(elemento) {
    if (!elemento.value.trim()) {
      mostrarError(elemento, 'Este campo es obligatorio');
      return false;
    } else {
      // No añadir la clase is-valid para evitar el check de validación
      elemento.classList.remove('is-invalid');
      return true;
    }
  }

  // Función para calcular la edad
  function calcularEdad(fechaNacimiento) {
    const hoy = new Date();
    const fechaNac = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - fechaNac.getFullYear();
    const mes = hoy.getMonth() - fechaNac.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < fechaNac.getDate())) {
      edad--;
    }
    return edad;
  }

  // Función para formatear la fecha de dd/mm/yyyy a yyyy-mm-dd
  function formatearFecha(fecha) {
    const [dia, mes, anio] = fecha.split('/');
    return `${anio}-${mes.padStart(2, '0')}-${dia.padStart(2, '0')}`;
  }

  // Función para consultar la API de CEE (Carnet de Extranjería)
  async function consultarCEE(numeroCEE) {
    try {
      // Cambiar el estado del botón a "cargando"
      actualizarEstadoBotonValidacion('loading');

      const options = {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${API_TOKEN_CEE}`
        }
      };

      const response = await fetch(`https://api.factiliza.com/v1/cee/info/${numeroCEE}`, options);
      const data = await response.json();

      if (data && data.success && data.data) {
        // Autocompletar los campos
        const personaData = data.data;

        // Verificar la estructura de los datos recibidos
        console.log("Datos de persona:", personaData);

        // Asignar los valores según la estructura exacta de la API
        apellidoPaterno.value = personaData.apellido_paterno || '';
        apellidoMaterno.value = personaData.apellido_materno || '';
        nombreCompleto.value = personaData.nombres || '';

        // Asegurarse de que fecha de nacimiento y género estén desbloqueados
        fechaNacimiento.readOnly = false;
        fechaNacimiento.disabled = false;
        fechaNacimiento.style.backgroundColor = '';
        fechaNacimiento.style.userSelect = '';
        fechaNacimiento.style.pointerEvents = '';

        genero.style.pointerEvents = '';
        genero.style.backgroundColor = '';

        // Bloquear solo los campos que se autocompletaron
        apellidoPaterno.readOnly = true;
        apellidoMaterno.readOnly = true;
        nombreCompleto.readOnly = true;

        // Cambiar el fondo de los campos a gris específico y deshabilitar la selección
        [apellidoPaterno, apellidoMaterno, nombreCompleto].forEach(campo => {
          campo.style.backgroundColor = '#e8e8e8';
          campo.style.userSelect = 'none';
          campo.style.pointerEvents = 'none';
          // Asegurarse de que no tengan la clase is-valid para evitar el check y el borde verde
          campo.classList.remove('is-valid');
          // Eliminar cualquier feedback de validación
          const feedback = campo.parentElement.querySelector('.valid-feedback');
          if (feedback) {
            feedback.remove();
          }
        });

        // No mostrar el check de validación en el campo documento
        documento.classList.remove('is-invalid');

        // Marcar el documento como validado
        documentoValidado = true;

        // Cambiar el estado del botón a "éxito"
        actualizarEstadoBotonValidacion('success');

        // Mostrar mensaje temporal de éxito
        mostrarMensajeTemporal('Datos de Carnet de Extranjería recuperados. Complete la fecha de nacimiento y género manualmente.', 'success', 5000);
      } else {
        // Mostrar mensaje de error en lugar de alert
        mostrarMensajeTemporal('No se encontraron datos para el Carnet de Extranjería ingresado. Complete los campos manualmente.', 'warning', 5000);
        documentoValidado = false;
        // Restaurar el estado del botón a "por defecto"
        actualizarEstadoBotonValidacion('default');

        // Desbloquear los campos para permitir edición manual
        [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento, genero].forEach(campo => {
          campo.readOnly = false;
          campo.disabled = false;
          campo.style.backgroundColor = '';
          campo.style.userSelect = '';
          campo.style.pointerEvents = '';
        });
      }
    } catch (error) {
      console.error('Error al consultar la API de CEE:', error);
      // Mostrar mensaje de error en lugar de alert
      mostrarMensajeTemporal('Error al consultar los datos del Carnet de Extranjería. Complete los campos manualmente.', 'danger', 5000);
      documentoValidado = false;
      // Restaurar el estado del botón a "por defecto"
      actualizarEstadoBotonValidacion('default');

      // Desbloquear los campos para permitir edición manual
      [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento, genero].forEach(campo => {
        campo.readOnly = false;
        campo.disabled = false;
        campo.style.backgroundColor = '';
        campo.style.userSelect = '';
        campo.style.pointerEvents = '';
      });
    }
  }

  // Función para consultar la API de DNI
  async function consultarDNI(dni) {
    try {
      // Cambiar el estado del botón a "cargando"
      actualizarEstadoBotonValidacion('loading');

      const response = await fetch(`https://api.perudevs.com/api/v1/dni/complete?document=${dni}&key=${API_KEY_DNI}`);
      const data = await response.json();

      if (data.estado && data.resultado) {
        // Autocompletar los campos
        apellidoPaterno.value = data.resultado.apellido_paterno;
        apellidoMaterno.value = data.resultado.apellido_materno;
        nombreCompleto.value = data.resultado.nombres;
        fechaNacimiento.value = formatearFecha(data.resultado.fecha_nacimiento);

        const generoValue = data.resultado.genero === 'M' ? '1' : '2';
        genero.value = generoValue;

        // Estilos para campos de DNI autocompletados y readonly
        [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento].forEach(campo => {
          if (campo) {
            campo.readOnly = true;
            campo.style.backgroundColor = '#e8e8e8';
            campo.style.userSelect = 'none';
            campo.style.pointerEvents = 'none';
            campo.style.border = '1px solid #dee2e6'; // Borde consistente
            campo.classList.remove('is-valid');
            const feedback = campo.parentElement.querySelector('.valid-feedback');
            if (feedback) feedback.remove();
          }
        });
        // Estilos específicos para el campo genero (select)
        if (genero) {
          genero.style.pointerEvents = 'none'; 
          genero.style.backgroundColor = '#e8e8e8';
          genero.style.border = '1px solid #dee2e6'; // Borde consistente
          genero.classList.remove('is-valid');
        }

        documento.classList.remove('is-invalid');
        documentoValidado = true;
        actualizarEstadoBotonValidacion('success');
        mostrarMensajeTemporal('Datos RENIEC recuperados', 'success', 3000);

        // Intentar obtener RUC para el DNI
        try {
          const rucResponse = await fetch(`https://api.perudevs.com/api/v1/dni/ruc-validate?document=${dni}&key=${API_KEY_DNI}`);
          const rucData = await rucResponse.json();
          console.log("Respuesta API DNI/RUC-Validate:", rucData);

          if (rucData.estado && rucData.resultado && rucData.resultado.id) {
            if (rucInput) {
              rucInput.value = rucData.resultado.id;
              rucInput.readOnly = true;
              rucInput.style.backgroundColor = '#e8e8e8';
              rucInput.style.userSelect = 'none';
              rucInput.style.pointerEvents = 'none';
              rucInput.style.border = '1px solid #dee2e6'; // Borde consistente
              rucInput.classList.remove('is-valid');
            }
            if (estadoInput) {
              estadoInput.value = rucData.resultado.estado || 'No especificado';
              estadoInput.readOnly = true;
              estadoInput.style.backgroundColor = '#e8e8e8';
              estadoInput.style.userSelect = 'none';
              estadoInput.style.pointerEvents = 'none';
              estadoInput.style.border = '1px solid #dee2e6'; // Borde consistente
              estadoInput.classList.remove('is-valid');
            }
            if (validarRucButton) validarRucButton.style.display = 'none';
            if (rucErrorMensaje) rucErrorMensaje.textContent = ''; 
            console.log('RUC encontrado y autocompletado para DNI:', rucData.resultado.id);
          } else { // RUC no encontrado o error leve en API RUC
            console.log('No se encontró RUC para el DNI (API dni/ruc-validate):', rucData.mensaje || JSON.stringify(rucData));
            if (rucInput) {
              rucInput.value = '';
              rucInput.placeholder = 'RUC (Opcional)';
              rucInput.readOnly = false;
              rucInput.style.backgroundColor = ''; 
              rucInput.style.userSelect = '';   
              rucInput.style.pointerEvents = '';
              rucInput.style.border = ''; // Resetear borde
            }
            if (estadoInput) { // Estado RUC siempre es readonly, pero se limpia y se le da estilo de readonly vacío
              estadoInput.value = '';
              estadoInput.readOnly = true; 
              estadoInput.style.backgroundColor = '#e8e8e8'; 
              estadoInput.style.userSelect = 'none';
              estadoInput.style.pointerEvents = 'none';
              estadoInput.style.border = '1px solid #dee2e6'; // Borde consistente para readonly vacío
            }
            if (validarRucButton) validarRucButton.style.display = 'block'; 
            if (rucErrorMensaje) rucErrorMensaje.textContent = rucData.mensaje === "Encontrado" && !rucData.resultado.id ? 'No se encontró RUC para este DNI.' : (rucData.mensaje || 'RUC no encontrado. Ingrese manualmente.');
          }
        } catch (rucError) { // Error grave al consultar API RUC
          console.error('Error grave al consultar API de DNI/RUC-Validate:', rucError);
          if (rucInput) {
            rucInput.value = '';
            rucInput.placeholder = 'Error al buscar RUC';
            rucInput.readOnly = false;
            rucInput.style.backgroundColor = ''; 
            rucInput.style.userSelect = '';   
            rucInput.style.pointerEvents = '';
            rucInput.style.border = ''; // Resetear borde
          }
          if (estadoInput) { // Estado RUC siempre es readonly
            estadoInput.value = '';
            estadoInput.readOnly = true;
            estadoInput.style.backgroundColor = '#e8e8e8';
            estadoInput.style.userSelect = 'none';
            estadoInput.style.pointerEvents = 'none';
            estadoInput.style.border = '1px solid #dee2e6'; // Borde consistente para readonly vacío
          }
          if (validarRucButton) validarRucButton.style.display = 'block';
          if (rucErrorMensaje) rucErrorMensaje.textContent = 'Error al consultar RUC. Ingrese manualmente.';
        }

      } else { // Si la consulta DNI falla
        mostrarMensajeTemporal(data.message || 'No se encontraron datos para el DNI ingresado. Complete los campos manualmente.', 'warning', 5000);
        documentoValidado = false;
        actualizarEstadoBotonValidacion('default');

        // Resetear campos DNI a editables
        [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento].forEach(campo => {
          if (campo) { 
            campo.readOnly = false;
            campo.style.backgroundColor = '';
            campo.style.userSelect = '';
            campo.style.pointerEvents = '';
            campo.style.border = ''; // Resetear borde
          }
        });
        if (genero) {
          genero.style.pointerEvents = '';
          genero.style.backgroundColor = '';
          genero.style.border = ''; // Resetear borde
        }

        // Resetear campos RUC a editables/estado inicial
        if (rucInput) {
          rucInput.value = '';
          rucInput.placeholder = 'RUC (Opcional)';
          rucInput.readOnly = false;
          rucInput.style.backgroundColor = '';
          rucInput.style.userSelect = '';
          rucInput.style.pointerEvents = '';
          rucInput.style.border = ''; // Resetear borde
        }
        if (estadoInput) { // Estado RUC siempre es readonly
          estadoInput.value = '';
          estadoInput.readOnly = true;
          estadoInput.style.backgroundColor = '#e8e8e8';
          estadoInput.style.userSelect = 'none';
          estadoInput.style.pointerEvents = 'none';
          estadoInput.style.border = '1px solid #dee2e6'; // Borde consistente para readonly vacío
        }
        if (validarRucButton) validarRucButton.style.display = 'block';
        if (rucErrorMensaje) rucErrorMensaje.textContent = '';
      }
    } catch (error) { // Si hay error en la llamada fetch principal de DNI
      console.error('Error al consultar la API de DNI:', error);
      mostrarMensajeTemporal('Error al consultar los datos del DNI. Complete los campos manualmente.', 'danger', 5000);
      documentoValidado = false;
      actualizarEstadoBotonValidacion('default');

      // Resetear todos los campos a editables/estado inicial
      [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento, rucInput].forEach(campo => {
        if (campo) {
          campo.readOnly = false;
          campo.style.backgroundColor = '';
          campo.style.userSelect = '';
          campo.style.pointerEvents = '';
          campo.style.border = ''; // Resetear borde
          if (campo === rucInput) campo.placeholder = 'RUC (Opcional)';
        }
      });
      if (genero) {
        genero.style.pointerEvents = '';
        genero.style.backgroundColor = '';
        genero.style.border = ''; // Resetear borde
      }
      
      if (estadoInput) { // Estado RUC siempre es readonly
        estadoInput.value = '';
        estadoInput.readOnly = true;
        estadoInput.style.backgroundColor = '#e8e8e8';
        estadoInput.style.userSelect = 'none';
        estadoInput.style.pointerEvents = 'none';
        estadoInput.style.border = '1px solid #dee2e6'; // Borde consistente para readonly vacío
      }
      if (validarRucButton) validarRucButton.style.display = 'block';
      if (rucErrorMensaje) rucErrorMensaje.textContent = '';
    }
  }

  if (tipoDocumento && documento && correo && telefono && fechaNacimiento) {
    // Configurar el botón de validación inicialmente
    if (validarDNIBtn) {
      validarDNIBtn.style.display = 'none';
      // Guardar el texto original del botón
      validarDNIBtn.setAttribute('data-original-text', validarDNIBtn.innerHTML);
      actualizarEstadoBotonValidacion('default');
    }

    // Evento para consultar DNI cuando se selecciona DNI y se ingresa el número
    tipoDocumento.addEventListener('change', function () {
      // No hacer nada si estamos en modo edición
      if (esEdicion) {
        return;
      }

      // Limpiar el campo de documento
      documento.value = '';

      // Reiniciar todos los campos de datos personales
      reiniciarCamposPersonales();
      limpiarCamposRucEstado(); // Limpiar también campos de RUC/Estado

      // Restaurar placeholders (se definirán después de que el DOM esté listo)
      if (originalPlaceholders.documento && documento) documento.placeholder = originalPlaceholders.documento;
      if (originalPlaceholders.ruc && rucInput) rucInput.placeholder = originalPlaceholders.ruc;
      if (originalPlaceholders.estado && estadoInput) estadoInput.placeholder = originalPlaceholders.estado;

      // Por defecto, RUC/Estado y su botón de validación están ocultos/deshabilitados
      if (rucInput) rucInput.readOnly = true;
      if (estadoInput) estadoInput.readOnly = true;
      if (validarRucButton) validarRucButton.style.display = 'none';

      const selectedTipo = tipoDocumento.options[tipoDocumento.selectedIndex].text;
      console.log("Tipo de documento seleccionado:", selectedTipo); // Añadir log para depuración

      if (selectedTipo === "DNI") {
        // Configurar para DNI
        documento.setAttribute("maxlength", "8");
        documento.setAttribute("pattern", "\\d{8}");
        documento.setAttribute("title", "Debe contener 8 dígitos numéricos");

        // Mantener los campos bloqueados hasta validación o error en la API
        toggleCamposPersonales(true);

        // Mostrar el botón de validación
        if (validarDNIBtn) {
          validarDNIBtn.style.display = 'block';
          actualizarEstadoBotonValidacion('default');
        }
      } else if (selectedTipo === "Carnet de extranjeria") {
        // Habilitar campos RUC y su botón de validación para Carnet de Extranjería
        if (rucInput) rucInput.readOnly = false;
        if (validarRucButton) validarRucButton.style.display = 'block';
        // Configurar para Carnet de Extranjería
        documento.setAttribute("maxlength", "9");
        documento.setAttribute("pattern", "\\d{9}");
        documento.setAttribute("title", "Debe contener 9 dígitos numéricos");

        // Mantener los campos bloqueados hasta validación o error en la API
        toggleCamposPersonales(true);

        // Mostrar el botón de validación
        if (validarDNIBtn) {
          validarDNIBtn.style.display = 'block';
          actualizarEstadoBotonValidacion('default');
        }
      } else if (selectedTipo === "Seleccione un tipo de documento") {
        // Si no se ha seleccionado un tipo de documento, bloquear los campos
        toggleCamposPersonales(true);

        // Ocultar el botón de validación
        if (validarDNIBtn) {
          validarDNIBtn.style.display = 'none';
        }
      } else {
        // Para otros tipos de documento, permitir entrada manual
        documento.setAttribute("maxlength", "9");
        documento.setAttribute("pattern", "\\d{9}");
        documento.setAttribute("title", "Debe contener 9 dígitos numéricos");

        // Desbloquear campos para otros tipos de documento
        toggleCamposPersonales(false);

        // Ocultar el botón de validación
        if (validarDNIBtn) {
          validarDNIBtn.style.display = 'none';
        }
      }
    });

    // Evento para cuando cambia el valor del documento
    documento.addEventListener('input', function () {
      // No hacer nada si estamos en modo edición
      if (esEdicion) {
        return;
      }

      // Reiniciar los campos de datos personales cuando cambia el documento
      reiniciarCamposPersonales();
      limpiarCamposRucEstado(); // Limpiar también campos de RUC/Estado

      // Restringir la entrada a solo números
      this.value = this.value.replace(/\D/g, "");

      // Asegurarse de que el botón vuelva a su estado original
      if (validarDNIBtn) {
        actualizarEstadoBotonValidacion('default');
      }
    });

    // Evento para el botón de validación de DNI
    if (validarDNIBtn) {
      validarDNIBtn.addEventListener('click', function () {
        // No hacer nada si estamos en modo edición
        if (esEdicion) {
          return;
        }

        const selectedTipo = tipoDocumento.options[tipoDocumento.selectedIndex].text;
        console.log("Validando documento tipo:", selectedTipo); // Añadir log para depuración

        if (selectedTipo === "DNI" && documento.value.length === 8) {
          consultarDNI(documento.value);
        } else if (selectedTipo === "DNI") {
          mostrarError(documento, 'El DNI debe contener 8 dígitos numéricos');
          documentoValidado = false;
        } else if (selectedTipo === "Carnet de extranjeria" && documento.value.length === 9) {
          consultarCEE(documento.value);
        } else if (selectedTipo === "Carnet de extranjeria") {
          mostrarError(documento, 'El Carnet de extranjeria debe contener 9 dígitos numéricos');
          documentoValidado = false;
        }
      });
    }

    // Restringir la entrada a solo números para el telefono
    telefono.addEventListener("input", function (e) {
      telefono.value = telefono.value.replace(/\D/g, "");
      telefono.setAttribute("maxlength", "9");
    });

    // Restringir la entrada a caracteres válidos para el correo electrónico excluyendo % y +
    correo.addEventListener("input", function (e) {
      correo.value = correo.value.replace(/[^a-zA-Z0-9._@]/g, "");
    });

    // Validación en tiempo real para campos de texto
    [apellidoPaterno, apellidoMaterno, nombreCompleto, direccion].forEach(campo => {
      if (campo) {
        campo.addEventListener('blur', function () {
          validarCampoRequerido(this);
        });
      }
    });

    // Validación en tiempo real para documento
    documento.addEventListener('blur', function () {
      if (!this.value) {
        mostrarError(this, 'El documento es obligatorio');
        return;
      }

      const selectedTipo = tipoDocumento.options[tipoDocumento.selectedIndex].text;
      if (selectedTipo === "DNI" && this.value.length !== 8) {
        mostrarError(this, 'El DNI debe contener 8 dígitos numéricos');
        return;
      } else if (selectedTipo === "Carnet de extranjeria" && this.value.length !== 9) {
        mostrarError(this, 'El Carnet de extranjeria debe contener 9 dígitos numéricos');
        return;
      } else if (selectedTipo !== "DNI" && selectedTipo !== "Carnet de extranjeria" && this.value.length !== 9) {
        mostrarError(this, 'El documento debe contener 9 dígitos numéricos');
        return;
      }

      // Para DNI y Carnet de extranjeria, no mostrar el check de validación
      if (selectedTipo === "DNI" || selectedTipo === "Carnet de extranjeria") {
        // Solo eliminar el error, no mostrar el check
        this.classList.remove('is-invalid');
      } else {
        // Para otros tipos de documento, mostrar el check si cumple con el formato
        if (this.value.length === 9) {
          mostrarValidacion(this);
        } else {
          // Solo eliminar el error, no mostrar el check
          this.classList.remove('is-invalid');
        }
      }
    });

    // Validación en tiempo real para teléfono
    telefono.addEventListener('blur', function () {
      if (!this.value) {
        mostrarError(this, 'El teléfono es obligatorio');
        return;
      }

      if (this.value.length < 6) {
        mostrarError(this, 'El teléfono debe tener al menos 6 dígitos');
        return;
      }

      // No mostrar check de validación, solo eliminar el error
      this.classList.remove('is-invalid');
    });

    // Validación en tiempo real para correo electrónico
    correo.addEventListener('blur', function () {
      if (!this.value) {
        mostrarError(this, 'El correo electrónico es obligatorio');
        return;
      }

      const emailPattern = /^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!emailPattern.test(this.value)) {
        mostrarError(this, 'Debe ser un correo electrónico válido (por ejemplo, usuario@dominio.com)');
        return;
      }

      // No mostrar check de validación, solo eliminar el error
      this.classList.remove('is-invalid');
    });

    // Validación en tiempo real para fecha de nacimiento
    fechaNacimiento.addEventListener('change', function () {
      if (!this.value) {
        mostrarError(this, 'La fecha de nacimiento es obligatoria');
        return;
      }

      const fechaNac = new Date(this.value);
      const hoy = new Date();

      // Verificar que la fecha de nacimiento no sea en el futuro
      if (fechaNac > hoy) {
        mostrarError(this, 'La fecha de nacimiento no puede ser en el futuro');
        return;
      }

      // Verificar edad mínima (18 años)
      const edad = calcularEdad(this.value);
      if (edad < 18) {
        mostrarError(this, 'El trabajador debe tener al menos 18 años');
        return;
      }

      // Verificar edad máxima (65 años)
      if (edad > 65) {
        mostrarError(this, 'El trabajador no puede tener más de 65 años');
        return;
      }

      // No mostrar check de validación, solo eliminar el error
      this.classList.remove('is-invalid');
    });

    // Validar al enviar el formulario
    document.querySelector("form").addEventListener("submit", function (event) {
      let formularioValido = true;

      // Si estamos en modo edición, permitir el envío sin validar los campos bloqueados
      if (esEdicion) {
        // Solo validar los campos que no están bloqueados
        if (correo && !correo.readOnly && !validarCampoRequerido(correo)) {
          formularioValido = false;
        }

        if (telefono && !telefono.readOnly && !validarCampoRequerido(telefono)) {
          formularioValido = false;
        }

        if (direccion && !direccion.readOnly && !validarCampoRequerido(direccion)) {
          formularioValido = false;
        }

        // Validar campos de ubicación
        if (departamento && !departamento.disabled && !validarCampoRequerido(departamento)) {
          formularioValido = false;
        }

        if (provincia && !provincia.disabled && !validarCampoRequerido(provincia)) {
          formularioValido = false;
        }

        if (distrito && !distrito.disabled && !validarCampoRequerido(distrito)) {
          formularioValido = false;
        }

        // Validar campos laborales
        if (tipoContrato && !tipoContrato.disabled && !validarCampoRequerido(tipoContrato)) {
          formularioValido = false;
        }

        // Validar correo electrónico
        const emailPattern = /^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (correo && !correo.readOnly && (!correo.checkValidity() || !emailPattern.test(correo.value))) {
          event.preventDefault();
          mostrarError(correo, 'Debe ser un correo electrónico válido (por ejemplo, usuario@dominio.com)');
          formularioValido = false;
        }
      } else {
        // Validación normal para nuevo registro
        // Validar campos requeridos
        [apellidoPaterno, apellidoMaterno, nombreCompleto, direccion].forEach(campo => {
          if (campo && !validarCampoRequerido(campo)) {
            formularioValido = false;
          }
        });

        // Validar campos de ubicación
        if (departamento && !validarCampoRequerido(departamento)) {
          formularioValido = false;
        }

        if (provincia && !validarCampoRequerido(provincia)) {
          formularioValido = false;
        }

        if (distrito && !validarCampoRequerido(distrito)) {
          formularioValido = false;
        }

        // Usar la misma expresión regular que en el HTML
        const emailPattern = /^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!documento.checkValidity()) {
          event.preventDefault();
          mostrarError(documento, documento.validationMessage);
          formularioValido = false;
        }
        if (!correo.checkValidity() || !emailPattern.test(correo.value)) {
          event.preventDefault();
          mostrarError(correo, 'Debe ser un correo electrónico válido (por ejemplo, usuario@dominio.com)');
          formularioValido = false;
        }

        // Validación de fecha de nacimiento
        if (fechaNacimiento.value) {
          const fechaNac = new Date(fechaNacimiento.value);
          const hoy = new Date();

          // Verificar que la fecha de nacimiento no sea en el futuro
          if (fechaNac > hoy) {
            event.preventDefault();
            mostrarError(fechaNacimiento, 'La fecha de nacimiento no puede ser en el futuro');
            formularioValido = false;
          }

          // Verificar edad mínima (18 años)
          const edad = calcularEdad(fechaNacimiento.value);
          if (edad < 18) {
            event.preventDefault();
            mostrarError(fechaNacimiento, 'El trabajador debe tener al menos 18 años');
            formularioValido = false;
          }

          // Verificar edad máxima (65 años)
          if (edad > 65) {
            event.preventDefault();
            mostrarError(fechaNacimiento, 'El trabajador no puede tener más de 65 años');
            formularioValido = false;
          }
        } else {
          mostrarError(fechaNacimiento, 'La fecha de nacimiento es obligatoria');
          formularioValido = false;
        }
      }

      if (!formularioValido) {
        event.preventDefault();
        // Desplazar a la primera posición con error
        const primerError = document.querySelector('.is-invalid');
        if (primerError) {
          primerError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
      }
    });
  } else {
    console.error("Alguno de los elementos no se encontró en el DOM.");
  }

  // Navegación entre pestañas
  if (siguienteContactoBtn) {
    siguienteContactoBtn.addEventListener('click', function () {
      document.getElementById('contacto-tab').click();
    });
  }

  if (siguienteLaboralBtn) {
    siguienteLaboralBtn.addEventListener('click', function () {
      document.getElementById('laboral-tab').click();
    });
  }

  if (anteriorDatosPersonalesBtn) {
    anteriorDatosPersonalesBtn.addEventListener('click', function () {
      document.getElementById('datos-personales-tab').click();
    });
  }

  if (anteriorContactoBtn) {
    anteriorContactoBtn.addEventListener('click', function () {
      document.getElementById('contacto-tab').click();
    });
  }

  // Event listener para rucInput (formateo y limpieza de error)
  if (rucInput && tipoDocumento) {
    rucInput.addEventListener('input', function (event) {
      const selectedOption = tipoDocumento.options[tipoDocumento.selectedIndex];
      const selectedOptionText = selectedOption ? selectedOption.text : '';
      if (selectedOptionText === 'Carnet de extranjeria') {
        let numericValue = this.value.replace(/[^0-9]/g, '');
        this.value = numericValue;
      }
      if (rucErrorMensaje) {
        rucErrorMensaje.textContent = '';
        rucErrorMensaje.style.display = 'none';
      }
    });
  }

  // Event listener para validarRucButton
  if (validarRucButton && rucInput && estadoInput && rucErrorMensaje && nombreCompleto && direccion && tipoDocumento) {
    validarRucButton.addEventListener('click', async function () {
      if (rucErrorMensaje) {
        rucErrorMensaje.textContent = '';
        rucErrorMensaje.style.display = 'none';
      }
      const ruc = rucInput.value;
      // Usamos API_KEY_DNI ya que es la misma que la 'apiKey' del script original
      if (ruc && ruc.length === 11 && /^[0-9]+$/.test(ruc)) {
        console.log('Validando RUC (desde validacionformulariotrabajador.js):', ruc);
        try {
          const urlApiRuc = `https://api.perudevs.com/api/v1/ruc?document=${ruc}&key=${API_KEY_DNI}`;
          const response = await fetch(urlApiRuc);
          if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);
          const data = await response.json();

          if (data.estado && data.resultado) {
            console.log('Datos de API (RUC):', data.resultado);
            estadoInput.value = data.resultado.estado || 'No especificado';

            rucInput.classList.remove('is-invalid');
            // rucInput.classList.add('is-valid'); // Opcional: check verde
            if (rucErrorMensaje) {
              rucErrorMensaje.textContent = 'RUC validado.';
              rucErrorMensaje.className = 'valid-feedback';
              rucErrorMensaje.style.display = 'block';
            }
            // Considerar si bloquear rucInput y estadoInput después de una validación exitosa de RUC
            // rucInput.readOnly = true;
            // estadoInput.readOnly = true;

          } else {
            if (rucErrorMensaje) {
              rucErrorMensaje.textContent = 'No se pudo validar el RUC: ' + (data.mensaje || 'Respuesta no exitosa o formato incorrecto.');
              rucErrorMensaje.className = 'invalid-feedback';
              rucErrorMensaje.style.display = 'block';
            }
            rucInput.classList.add('is-invalid');
            rucInput.classList.remove('is-valid');
            if (estadoInput) estadoInput.value = '';
          }
        } catch (error) {
          console.error('Error al validar RUC:', error);
          if (rucErrorMensaje) {
            rucErrorMensaje.textContent = 'Error al conectar con el servicio de validación de RUC.';
            rucErrorMensaje.className = 'invalid-feedback';
            rucErrorMensaje.style.display = 'block';
          }
          rucInput.classList.add('is-invalid');
          rucInput.classList.remove('is-valid');
          if (estadoInput) estadoInput.value = '';
        }
      } else {
        if (rucErrorMensaje) {
          rucErrorMensaje.textContent = 'Por favor, ingrese un RUC válido de 11 dígitos numéricos.';
          rucErrorMensaje.className = 'invalid-feedback';
          rucErrorMensaje.style.display = 'block';
        }
        if (rucInput) {
          rucInput.classList.add('is-invalid');
          rucInput.classList.remove('is-valid');
        }
      }
    });
  }

  // Inicialización de placeholders y estado de campos al cargar la página
  // Asegurarse que los elementos existen antes de acceder a sus propiedades
  if (documento && rucInput && estadoInput) {
    originalPlaceholders = {
      documento: documento.placeholder || '',
      ruc: rucInput.placeholder || '',
      estado: estadoInput.placeholder || ''
    };
  }

  // Simular un evento change en tipoDocumento para establecer el estado inicial de los campos
  if (tipoDocumento && typeof tipoDocumento.dispatchEvent === 'function') {
    if (esEdicion) {
      const selectedOption = tipoDocumento.options[tipoDocumento.selectedIndex];
      const selectedOptionText = selectedOption ? selectedOption.text : '';
      if (selectedOptionText === 'Carnet de extranjeria') {
        if (rucInput) rucInput.readOnly = false;
        if (validarRucButton) validarRucButton.style.display = 'block';
      } else {
        if (rucInput) rucInput.readOnly = true;
        if (validarRucButton) validarRucButton.style.display = 'none';
      }
      // Si es DNI y está en edición, el botón validarDNI debe estar oculto o en estado validado
      // El código actual en esEdicion ya oculta validarDNIBtn, lo que es correcto.
    } else {
      tipoDocumento.dispatchEvent(new Event('change'));
    }
  }
});
