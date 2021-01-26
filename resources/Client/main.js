/*all my icons have a class of option so I have to use query selector ALL which will put them into a notelist which is similar to an array*/
const options = document.querySelectorAll('.option');
const score = document.getElementById('score');
const result = document.getElementById('result');
const quit = document.getElementById('quit');
const modal = document.querySelector('.modal');
/*let PlayerID = getPlayerID();*/
let UserName = document.getElementById("UserName");
let Password = document.getElementById("Password");

let scoreboard = {
    player: 0,
    computer: 0
}

/*play game*/
function play(e) { /*passing the event parameter*/
    quit.style.display = 'inline'; /*so that the player has a choice to quit once the round has started*/
    const playerChoice = e.target.id; /*getting by id so we can distinguish what was chosen*/
    /*for the computer's choice*/
    const computerChoice = getComputerChoice();
    const winner = getWinner(playerChoice, computerChoice);
    console.log(playerChoice, computerChoice, winner); /*testing*/
    showWinner(winner, computerChoice);
}

/*get computer's choice, I'm using a subroutine to keep my code tidy and efficient*/
function getComputerChoice() {
    const rand = Math.random(); /*gives a random decimal to determine computer's choice*/
    if(rand < 0.34) { /*ensures a random, even chance of either move*/
        return 'rock';
    } else if(rand <= 0.67) {
        return 'scissors';
    } else {
        return 'paper';
    }
}

/*get game winner*/
function getWinner(p, c) {
    if(p === c) {
        return 'draw';
    } else if (p === 'rock') {
        if(c==='paper') {
            return 'computer';
        } else {
            return 'player';
        }
    }
    else if(p==='paper') {
        if(c==='scissors') {
            return 'computer'
        } else {
            return 'player';
        }
    }
    else if(p==='scissors') {
        if(c==='rock') {
            return 'computer';
        }
        else {
            return 'player';
        }
    }
}

function showWinner(winner, computerChoice) {
    if(winner==='player') {
        /*increment player score*/
        scoreboard.player++;
        /*this is where results/update will be used*/
        /*show modal result*/
        result.innerHTML = `
            <h1 class="text-win">You Win!</h1> 
            <i class="fas fa-hand-${computerChoice} fa-10x"></i> <!--dynamically chooses the computer's choice-->
            <p>Computer chose <strong>${computerChoice}</strong></p>
        `;
    } else if(winner==='computer') {
        scoreboard.computer++;
        result.innerHTML = `
            <h1 class="text-lose">You Lose!</h1> 
            <i class="far fa-hand-${computerChoice} fa-10x"></i> <!--dynamically chooses the computer's choice-->
            <p>Computer chose <strong>${computerChoice}</strong></p>
        `;
    } else {
        result.innerHTML = `
            <h1>It's A Draw</h1> 
            <i class="fas fa-hand-${computerChoice} fa-10x"></i> <!--dynamically chooses the computer's choice-->
            <p>Computer chose <strong>${computerChoice}</strong></p>
        `;
    }
    /*show score. This function increases the score, shows the modal and shows the score.*/
    score.innerHTML = `
    <p>Player: ${scoreboard.player}</p>
    <p>Computer: ${scoreboard.computer}</p>
    `;
    
    modal.style.display = 'block';
    
}
function clearModal(e) {
    if(e.target===modal) {
        modal.style.display = 'none';
    }
}
/*event listeners*/
/*loop through the options using a for loop*/
options.forEach(option => option.addEventListener('click', play)); /*this essentially waits for the player to click a button*/
window.addEventListener('click', clearModal); /*to exit the modal*/

function registerPlayer() {
    console.log("Invoked AddPlayer()");
    const formData = new FormData(document.getElementById('InputPlayerDetailsR'));
    let url = "/Players/add";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            window.open("http://localhost:8081/client/game.html", "_self");   //URL replaces the current page.  Create a new html file
        }                                                  //in the client folder called welcome.html
    });
}

function getPScore(){   //API used to fetch wins so that it can be the initial score
    console.log("Invoked getPScore()");
    let url="/Results/get_wins/{PlayerID}";
    fetch(url,{
        method:"GET",
    }).then(response=>{
        return response.json();
    }).then(response=>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response));
        } else{
            return(response.Wins);
            console.log(response.Wins);
        }
    })

}

function loginPlayer() {
    console.log("Invoked loginPlayer()");
    const formData = new FormData(document.getElementById('InputPlayerDetailsL'));
    let url = "/Players/login";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            Cookies.set("PlayerID", response.PlayerID);
            window.open("http://localhost:8081/client/game.html", "_self");   //URL replaces the current page.  Create a new html file
        }                                                  //in the client folder called welcome.html
    });
}

