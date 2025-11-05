document.addEventListener("DOMContentLoaded", () => {
    // navbar part
    const dropdownMore = document.getElementById("dropdown-more");
    const moreBtn = document.getElementById("more-btn");

    const menuCollapse = document.getElementById("menu-collapse");
    const menuCollapseOpen = document.getElementById("menu-collapse-open");
    const menuCollapseClose = document.getElementById("menu-collapse-close");

    const menuMore = document.getElementById("menu-more");
    const menuMoreBtn = document.getElementById("menu-more-btn");
    const menuMoreClose = document.getElementById("menu-more-close");

    // Handle dropdown visibility
    moreBtn.addEventListener("mouseenter", () => {
        dropdownMore.style.visibility = "visible";
        dropdownMore.style.opacity = "1";
    });

    moreBtn.addEventListener("mouseleave", () => {
        dropdownMore.style.visibility = "hidden";
        dropdownMore.style.opacity = "0";
    });

    dropdownMore.addEventListener("mouseenter", () => {
        dropdownMore.style.visibility = "visible";
        dropdownMore.style.opacity = "1";
    });

    dropdownMore.addEventListener("mouseleave", () => {
        dropdownMore.style.visibility = "hidden";
        dropdownMore.style.opacity = "0";
    });

    // Handle menu collapse open/close
    menuCollapseOpen.addEventListener("click", () => {
        menuCollapse.classList.add("menu_collapse_transition_open");
    });

    menuCollapseClose.addEventListener("click", () => {
        menuCollapse.classList.remove("menu_collapse_transition_open");
    });

    // Handle menu more open/close
    menuMoreBtn.addEventListener("click", () => {
        menuMore.classList.add("menu_collapse_transition_open");
    });

    menuMoreClose.addEventListener("click", () => {
        menuMore.classList.remove("menu_collapse_transition_open");
    });



    // slides part
    // Array of slides with images, titles, and descriptions
    const slides = [
        {
            image: "./assets/images/ny.jpg", // Update paths as needed
            title: "New York",
            description: "The atmosphere in New York is lorem ipsum."
        },
        {
            image: "./assets/images/la.jpg", // Update paths as needed
            title: "Los Angeles",
            description: "We had the best time playing at Venice Beach!"
        },
        {
            image: "./assets/images/chicago.jpg", // Update paths as needed
            title: "Chicago",
            description: "Thank you, Chicago - A night we won't forget."
        }
    ];

    let currentSlide = 0;

    // DOM elements
    const slideContainer = document.getElementById("slide-container");
    const slideTitle = document.getElementById("slide-title");
    const slideDescription = document.getElementById("slide-description");

    // Function to update the slide
    const updateSlide = () => {
        const current = slides[currentSlide];
        slideContainer.style.backgroundImage = `url(${current.image})`;
        slideTitle.textContent = current.title;
        slideDescription.textContent = current.description;
    };

    // Automatically change the slide every 5 seconds
    setInterval(() => {
        currentSlide = (currentSlide + 1) % slides.length;
        updateSlide();
    }, 5000);

    // Initialize the first slide
    updateSlide();



    // theband part
    // Data for band members
    const members = [
        { title: "Boima", img1: "./assets/images/Boima.jpg" },
        { title: "Boima", img1: "./assets/images/Boima.jpg" },
        { title: "Boima", img1: "./assets/images/Boima.jpg" }
    ];

    // Get the container where members will be added
    const membersContainer = document.getElementById("members-container");

    // Function to create a member element
    const createMember = (title, img1) => {
        // Create the main member div
        const memberDiv = document.createElement("div");
        memberDiv.classList.add("main_member");

        // Add the title
        const titleElement = document.createElement("h3");
        titleElement.textContent = title;
        memberDiv.appendChild(titleElement);

        // Add the image
        const imageDiv = document.createElement("div");
        imageDiv.classList.add("img1");
        imageDiv.style.backgroundImage = `url(${img1})`;
        memberDiv.appendChild(imageDiv);

        return memberDiv;
    };

    // Render each member
    members.forEach(member => {
        const memberElement = createMember(member.title, member.img1);
        membersContainer.appendChild(memberElement);
    });



    // tour part
    // Ticket data
    const tickets = [
        {
            img1: "./assets/images/newyork.jpg",
            para1: "New York",
            para2: "Fri 27 Nov 2016",
            para3: "Praesent tincidunt sed tellus ut rutrum sed vitae justo."
        },
        {
            img1: "./assets/images/paris.jpg",
            para1: "Paris",
            para2: "Sat 28 Nov 2016",
            para3: "Praesent tincidunt sed tellus ut rutrum sed vitae justo."
        },
        {
            img1: "./assets/images/sanfran.jpg",
            para1: "San Francisco",
            para2: "Sun 29 Nov 2016",
            para3: "Praesent tincidunt sed tellus ut rutrum sed vitae justo."
        }
    ];

    const ticketsContainer = document.getElementById("tickets-container");

    // Function to create a ticket
    const createTicket = ({ img1, para1, para2, para3 }) => {
        const ticketDiv = document.createElement("div");
        ticketDiv.classList.add("main_ticket");

        const imageDiv = document.createElement("div");
        imageDiv.classList.add("ticket_image");
        imageDiv.style.backgroundImage = `url(${img1})`;

        const ticketBox = document.createElement("div");
        ticketBox.classList.add("ticket_box");

        const title1 = document.createElement("p");
        title1.textContent = para1;

        const title2 = document.createElement("p");
        title2.textContent = para2;

        const title3 = document.createElement("p");
        title3.textContent = para3;

        const button = document.createElement("button");
        button.type = "button";
        button.textContent = "Buy Tickets";

        ticketBox.appendChild(title1);
        ticketBox.appendChild(title2);
        ticketBox.appendChild(title3);
        ticketBox.appendChild(button);

        ticketDiv.appendChild(imageDiv);
        ticketDiv.appendChild(ticketBox);

        return ticketDiv;
    };

    // Render tickets
    tickets.forEach(ticket => {
        const ticketElement = createTicket(ticket);
        ticketsContainer.appendChild(ticketElement);
    });




});
  