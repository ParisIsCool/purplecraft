const video = document.getElementById('background-video');
let lastScrollY = 0;
let lastCalledTime = Date.now();

// Function to update video time based on scroll
function updateVideoTime() {
    const scrollPosition = window.scrollY;
    const docHeight = document.body.offsetHeight;
    const maxScroll = document.body.scrollHeight - window.innerHeight;
    const videoDuration = video.duration
    
    // Assuming video is 10000px tall, calculate a ratio to scale scroll position to video time;
    const videoHeight = 6500;
    const scrollRatio = videoHeight / docHeight;

    // Calculate the video's current time based on the scroll position
    const videoCurrentTime = (scrollPosition / maxScroll) * (videoDuration / scrollRatio);

    // Set the video's current time if it's a valid number
    if (isFinite(videoCurrentTime) && !isNaN(videoCurrentTime)) {
        video.currentTime = videoCurrentTime;
    }

    // Print out FPS of these calls
    //console.log('FPS:', 1000 / (Date.now() - lastCalledTime));
    lastCalledTime = Date.now();
}

function onScroll() {
    lastScrollY = window.scrollY;
}

video.addEventListener('loadedmetadata', () => {
    // Start listening to the scroll event
    window.addEventListener('scroll', onScroll);
    // Begin updateVideoTime loop
});

// Calculate FPS
const interval = 1000 / 30;

// Use setInterval to run the function at the calculated interval
setInterval(updateVideoTime, interval);