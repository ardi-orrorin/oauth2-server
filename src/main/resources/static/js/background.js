const shapes = document.querySelectorAll('.shape');
shapes.forEach(shape => {
  setInterval(() => {
    const randomRotation = Math.random() * 360;
    const randomScale = 0.8 + Math.random() * 0.4;
    shape.style.transform = `rotate(${randomRotation}deg) scale(${randomScale})`;
  }, 3000 + Math.random() * 3000);
});