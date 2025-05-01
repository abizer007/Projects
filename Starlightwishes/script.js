var canvas = document.getElementById("sky");
var context = canvas.getContext("2d");
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
var stars = [];
var shootingStars = [];
for (var i = 0; i < 200; i++) {
    stars.push({
        x: Math.random() * canvas.width,
        y: Math.random() * canvas.height,
        size: Math.random() * 2,
        brightness: Math.random()
    });
}
function drawStars() {
    for (var _i = 0, stars_1 = stars; _i < stars_1.length; _i++) {
        var star = stars_1[_i];
        context.beginPath();
        context.arc(star.x, star.y, star.size, 0, Math.PI * 2);
        context.fillStyle = "rgba(255, 255, 255, ".concat(star.brightness, ")");
        context.fill();
        star.brightness += (Math.random() - 0.5) * 0.05;
        star.brightness = Math.max(0.1, Math.min(1, star.brightness));
    }
}
function drawShootingStars() {
    for (var i = shootingStars.length - 1; i >= 0; i--) {
        var s = shootingStars[i];
        context.beginPath();
        context.moveTo(s.x, s.y);
        context.lineTo(s.x - s.length * Math.cos(s.angle), s.y - s.length * Math.sin(s.angle));
        context.strokeStyle = "white";
        context.lineWidth = 2;
        context.stroke();
        s.x += s.speed * Math.cos(s.angle);
        s.y += s.speed * Math.sin(s.angle);
        if (s.x > canvas.width || s.y > canvas.height) {
            shootingStars.splice(i, 1);
        }
    }
}
function animate() {
    context.clearRect(0, 0, canvas.width, canvas.height);
    drawStars();
    drawShootingStars();
    if (Math.random() < 0.01) {
        shootingStars.push({
            x: Math.random() * canvas.width,
            y: 0,
            length: 100,
            speed: 8,
            angle: Math.PI / 4
        });
    }
    requestAnimationFrame(animate);
}
function typeMessage(msg) {
    var messageDiv = document.getElementById("message");
    var i = 0;
    var interval = setInterval(function () {
        messageDiv.textContent += msg.charAt(i);
        i++;
        if (i >= msg.length)
            clearInterval(interval);
    }, 60);
}
window.onload = function () {
    animate();
    setTimeout(function () {
        typeMessage("Success consists of going from failure to failure without loss of enthusiasm.");
    }, 3000);
};
