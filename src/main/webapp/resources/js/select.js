    function changeColumnColor(selectElement) {
        // Obtener la fila actual del select
        const row = selectElement.closest('tr');
        
        // Si el valor es "Incorrecto", agregar la clase 'error-row'
        if (selectElement.value === 'Incorrecto') {
            row.classList.add('error-row');
            console.log("red");
        } else {
            // Si es cualquier otro valor, quitar la clase 'error-row'
            row.classList.remove('error-row');
        }
    }
