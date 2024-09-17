document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.proyecto-card');
    let currentIndex = 0;
    const intervalTime = 3000; // Intervalo de 3 segundos (3000 milisegundos)
    let interval; // Variable para almacenar el identificador del intervalo

    function showCard(index) {
        const totalCards = cards.length;
        const prevIndex = (index - 1 + totalCards) % totalCards;
        const nextIndex = (index + 1) % totalCards;

        cards.forEach((card, i) => {
            card.classList.remove('active', 'prev', 'next');
            if (i === index) {
                card.classList.add('active');
            } else if (i === prevIndex) {
                card.classList.add('prev');
            } else if (i === nextIndex) {
                card.classList.add('next');
            }
        });
    }

    function showNextCard() {
        currentIndex = (currentIndex < cards.length - 1) ? currentIndex + 1 : 0;
        showCard(currentIndex);
    }

    // Función para iniciar el intervalo
    function startInterval() {
        interval = setInterval(showNextCard, intervalTime);
    }

    // Función para detener el intervalo
    function stopInterval() {
        clearInterval(interval);
    }

    // Iniciar el intervalo cuando se carga la página
    startInterval();

    // Detener el intervalo al colocar el mouse sobre una card
    cards.forEach(card => {
        card.addEventListener('mouseover', stopInterval);
        card.addEventListener('mouseout', startInterval);
    });

    // Detener el intervalo al hacer clic en los botones de navegación manual
    document.querySelector('.nav-button.left').addEventListener('click', () => {
        clearInterval(interval); // Detener el intervalo actual
        currentIndex = (currentIndex > 0) ? currentIndex - 1 : cards.length - 1;
        showCard(currentIndex);
        startInterval(); // Reiniciar el intervalo después de hacer clic
    });

    document.querySelector('.nav-button.right').addEventListener('click', () => {
        clearInterval(interval); // Detener el intervalo actual
        currentIndex = (currentIndex < cards.length - 1) ? currentIndex + 1 : 0;
        showCard(currentIndex);
        startInterval(); // Reiniciar el intervalo después de hacer clic
    });

    // Mostrar la primera card inicialmente
    showCard(currentIndex);
});
