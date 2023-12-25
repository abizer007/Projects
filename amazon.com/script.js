// JavaScript code goes here
// You can add your custom logic, event handlers, and functionality

// Example event handler for clicking the search icon
function handleSearch() {
    const searchInput = document.querySelector('.search-input');
    const searchTerm = searchInput.value;
    
    // Perform search or any other desired action
    console.log('Search term:', searchTerm);
  }
  
  // Example event handler for clicking "See more" in the boxes
  function handleSeeMore(event) {
    const boxContent = event.target.parentNode;
    const boxTitle = boxContent.querySelector('h2').textContent;
    
    // Perform action based on the box title
    console.log('See more:', boxTitle);
  }
  
  // Example event handler for clicking "Click here to go to amazon.in" link
  function handleLinkClick(event) {
    event.preventDefault();
    
    // Perform action to redirect to amazon.in or any other desired action
    console.log('Redirecting to amazon.in');
  }
  
  // Attach event handlers to respective elements
  document.querySelector('.search-icon').addEventListener('click', handleSearch);
  
  const seeMoreButtons = document.querySelectorAll('.box-content p');
  seeMoreButtons.forEach(button => {
    button.addEventListener('click', handleSeeMore);
  });
  
  const carouselItems = document.querySelectorAll('.carousel-item');
  let currentItemIndex = 0;
  
  function showNextItem() {
    carouselItems[currentItemIndex].classList.remove('active');
    currentItemIndex = (currentItemIndex + 1) % carouselItems.length;
    carouselItems[currentItemIndex].classList.add('active');
  }
  
  function showPreviousItem() {
    carouselItems[currentItemIndex].classList.remove('active');
    currentItemIndex = (currentItemIndex - 1 + carouselItems.length) % carouselItems.length;
    carouselItems[currentItemIndex].classList.add('active');
  }
  