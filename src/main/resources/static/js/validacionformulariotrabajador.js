document.addEventListener("DOMContentLoaded", function () {
  const tipoDocumento = document.getElementById("tipoDocumento");
  const documento = document.getElementById("documento");
  const correo = document.getElementById("correo");
  const telefono = document.getElementById("telefono");
  const fechaNacimiento = document.getElementById("fechaNacimiento");
  const fechaEstado = document.getElementById("fechaEstado");
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
    
    if (fechaEstado) {
      fechaEstado.readOnly = false;
      fechaEstado.disabled = false;
      fechaEstado.classList.remove('bg-light');
      fechaEstado.style.backgroundColor = '';
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
    mensajeElement.style.zIndex = '9999';
    mensajeElement.style.minWidth = '300px';
    mensajeElement.style.textAlign = 'center';
    mensajeElement.style.boxShadow = '0 4px 8px rgba(0,0,0,0.1)';
    mensajeElement.textContent = mensaje;
    
    // Insertar el mensaje en el body
    document.body.appendChild(mensajeElement);
    
    // Eliminar el mensaje después de la duración especificada
    setTimeout(() => {
      mensajeElement.remove();
    }, duracion);
  }
  
  // Función para actualizar el estado del botón de validación
  function actualizarEstadoBotonValidacion(estado) {
    if (!validarDNIBtn) return;
    
    // Eliminar clases anteriores
    validarDNIBtn.classList.remove('btn-secondary', 'btn-success', 'btn-primary');
    validarDNIBtn.classList.remove('spinner-border', 'spinner-border-sm');
    
    // Guardar el texto original
    const textoOriginal = validarDNIBtn.getAttribute('data-original-text') || 'Validar';
    
    // Establecer un ancho fijo para el botón
    validarDNIBtn.style.width = '140px';
    validarDNIBtn.style.minWidth = '140px';
    validarDNIBtn.style.maxWidth = '140px';
    validarDNIBtn.style.textAlign = 'center';
    validarDNIBtn.style.whiteSpace = 'nowrap';
    validarDNIBtn.style.overflow = 'visible';
    validarDNIBtn.style.textOverflow = 'clip';
    validarDNIBtn.style.padding = '0.375rem 0.75rem';
    
    switch (estado) {
      case 'default':
        validarDNIBtn.classList.add('btn-secondary');
        validarDNIBtn.innerHTML = textoOriginal;
        validarDNIBtn.disabled = false;
        break;
      case 'loading':
        validarDNIBtn.classList.add('btn-primary');
        validarDNIBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Cargando...';
        validarDNIBtn.disabled = true;
        break;
      case 'success':
        validarDNIBtn.classList.add('btn-success');
        validarDNIBtn.innerHTML = '<i class="bi bi-check-circle-fill me-1"></i> Validado';
        validarDNIBtn.disabled = true;
        break;
    }
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
      
      console.log("Respuesta de la API CEE:", data); // Añadir log para depuración
      
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
        
        // Seleccionar el género según el valor de la API
        const generoValue = data.resultado.genero === 'M' ? '1' : '2'; // Asumiendo que 1 es Masculino y 2 es Femenino
        genero.value = generoValue;
        
        // Bloquear los campos para evitar ediciones manuales
        apellidoPaterno.readOnly = true;
        apellidoMaterno.readOnly = true;
        nombreCompleto.readOnly = true;
        fechaNacimiento.readOnly = true;
        genero.style.pointerEvents = 'none';
        genero.style.backgroundColor = '#e8e8e8';
        
        // Cambiar el fondo de los campos a gris específico y deshabilitar la selección
        [apellidoPaterno, apellidoMaterno, nombreCompleto, fechaNacimiento, genero].forEach(campo => {
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
        
        // No mostrar el check de validación en el campo DNI
        // Solo eliminar la clase is-invalid si existe
        documento.classList.remove('is-invalid');
        
        // Marcar el DNI como validado
        documentoValidado = true;
        
        // Cambiar el estado del botón a "éxito"
        actualizarEstadoBotonValidacion('success');
        
        // Mostrar mensaje temporal de éxito
        mostrarMensajeTemporal('Datos RENIEC recuperados', 'success', 3000);
      } else {
        // Mostrar mensaje de error en lugar de alert
        mostrarMensajeTemporal('No se encontraron datos para el DNI ingresado. Complete los campos manualmente.', 'warning', 5000);
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
      console.error('Error al consultar la API:', error);
      // Mostrar mensaje de error en lugar de alert
      mostrarMensajeTemporal('Error al consultar los datos del DNI. Complete los campos manualmente.', 'danger', 5000);
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
  
  if (tipoDocumento && documento && correo && telefono && fechaNacimiento && fechaEstado) {
    // Configurar el botón de validación inicialmente
    if (validarDNIBtn) {
      validarDNIBtn.style.display = 'none';
      // Guardar el texto original del botón
      validarDNIBtn.setAttribute('data-original-text', validarDNIBtn.innerHTML);
      actualizarEstadoBotonValidacion('default');
    }
    
    // Evento para consultar DNI cuando se selecciona DNI y se ingresa el número
    tipoDocumento.addEventListener('change', function() {
      // No hacer nada si estamos en modo edición
      if (esEdicion) {
        return;
      }
      
      // Limpiar el campo de documento
      documento.value = '';
      
      // Reiniciar todos los campos de datos personales
      reiniciarCamposPersonales();
      
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
    documento.addEventListener('input', function() {
      // No hacer nada si estamos en modo edición
      if (esEdicion) {
        return;
      }
      
      // Reiniciar los campos de datos personales cuando cambia el documento
      reiniciarCamposPersonales();
      
      // Restringir la entrada a solo números
      this.value = this.value.replace(/\D/g, "");
      
      // Asegurarse de que el botón vuelva a su estado original
      if (validarDNIBtn) {
        actualizarEstadoBotonValidacion('default');
      }
    });
    
    // Evento para el botón de validación de DNI
    if (validarDNIBtn) {
      validarDNIBtn.addEventListener('click', function() {
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
        campo.addEventListener('blur', function() {
          validarCampoRequerido(this);
        });
      }
    });
    
    // Validación en tiempo real para documento
    documento.addEventListener('blur', function() {
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
    telefono.addEventListener('blur', function() {
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
    correo.addEventListener('blur', function() {
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
    fechaNacimiento.addEventListener('change', function() {
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
    
    // Validación en tiempo real para fecha de alta
    fechaEstado.addEventListener('change', function() {
      if (!this.value) {
        mostrarError(this, 'La fecha de alta es obligatoria');
        return;
      }
      
      const fechaAlta = new Date(this.value);
      const hoy = new Date();
      
      // Verificar que la fecha de alta no sea en el futuro
      if (fechaAlta > hoy) {
        mostrarError(this, 'La fecha de alta no puede ser en el futuro');
        return;
      }
      
      // Verificar que la fecha de alta sea posterior a la fecha de nacimiento
      if (fechaNacimiento.value) {
        const fechaNac = new Date(fechaNacimiento.value);
        if (fechaAlta < fechaNac) {
          mostrarError(this, 'La fecha de alta debe ser posterior a la fecha de nacimiento');
          return;
        }
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
        
        if (fechaEstado && !fechaEstado.readOnly && !validarCampoRequerido(fechaEstado)) {
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
      
      // Validación de fecha de alta
      if (fechaEstado.value) {
        const fechaAlta = new Date(fechaEstado.value);
        const hoy = new Date();
        
        // Verificar que la fecha de alta no sea en el futuro
        if (fechaAlta > hoy) {
          event.preventDefault();
          mostrarError(fechaEstado, 'La fecha de alta no puede ser en el futuro');
          formularioValido = false;
        }
        
        // Verificar que la fecha de alta sea posterior a la fecha de nacimiento
        if (fechaNacimiento.value) {
          const fechaNac = new Date(fechaNacimiento.value);
          if (fechaAlta < fechaNac) {
            event.preventDefault();
            mostrarError(fechaEstado, 'La fecha de alta debe ser posterior a la fecha de nacimiento');
            formularioValido = false;
          }
        }
      } else {
        mostrarError(fechaEstado, 'La fecha de alta es obligatoria');
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
    siguienteContactoBtn.addEventListener('click', function() {
      document.getElementById('contacto-tab').click();
    });
  }
  
  if (siguienteLaboralBtn) {
    siguienteLaboralBtn.addEventListener('click', function() {
      document.getElementById('laboral-tab').click();
    });
  }
  
  if (anteriorDatosPersonalesBtn) {
    anteriorDatosPersonalesBtn.addEventListener('click', function() {
      document.getElementById('datos-personales-tab').click();
    });
  }
  
  if (anteriorContactoBtn) {
    anteriorContactoBtn.addEventListener('click', function() {
      document.getElementById('contacto-tab').click();
    });
  }
});
