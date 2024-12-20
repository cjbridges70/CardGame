// Define a known set of cards that exist in the game
const CARD_LIBRARY = [
    {id: 'C001', name: 'Luminarii Sproutling', faction: 'Luminarii', attack: 2, health: 5},
    {id: 'C002', name: 'Ferrum Ironclad', faction: 'Ferrum', attack: 5, health: 3},
    {id: 'C003', name: 'Chromius Hackblade', faction: 'Chromius', attack: 3, health: 4},
    {id: 'C004', name: 'Oraceli Seer', faction: 'Oraceli', attack: 1, health: 6},
    {id: 'C005', name: 'Junker Scraprider', faction: 'Junkers', attack: 4, health: 2}
];

// Card class
class Card {
    constructor(id, name, faction, attack, health) {
        this.id = id;
        this.name = name;
        this.faction = faction;
        this.attack = attack;
        this.health = health;
    }
}

// Player class handling inventory, deck, and hand
class Player {
    constructor() {
        this.inventory = [];
        this.deck = [];
        this.hand = [];
    }

    addCardToInventory(card) {
        this.inventory.push(card);
    }

    addCardToDeck(cardId) {
        const cardIndex = this.inventory.findIndex(c => c.id === cardId);
        if (cardIndex > -1) {
            this.deck.push(this.inventory[cardIndex]);
            // Optionally remove the card from inventory:
            // this.inventory.splice(cardIndex, 1);
        }
    }

    getCardFromDeck(cardId) {
        return this.deck.find(c => c.id === cardId);
    }

    drawCard() {
        if (this.deck.length > 0) {
            const drawnCard = this.deck.pop();
            this.hand.push(drawnCard);
            return drawnCard;
        } else {
            return null;
        }
    }
}

// Instead of random generation, pick a random card from our known library
function getRandomLibraryCard() {
    const randomIndex = Math.floor(Math.random() * CARD_LIBRARY.length);
    const base = CARD_LIBRARY[randomIndex];
    return new Card(base.id, base.name, base.faction, base.attack, base.health);
}

// Simple battle function
// For this example, we’ll say the winner is determined by (attack - opponent's health)
function battleCards(card1, card2) {
    const card1Score = card1.attack - card2.health;
    const card2Score = card2.attack - card1.health;

    if (card1Score > card2Score) {
        return `${card1.name} (ID: ${card1.id}) wins!`;
    } else if (card2Score > card1Score) {
        return `${card2.name} (ID: ${card2.id}) wins!`;
    } else {
        return "It's a draw!";
    }
}

const player = new Player();

// DOM references
const inventoryEl = document.getElementById('inventory');
const deckEl = document.getElementById('deck');
const handEl = document.getElementById('hand');

const obtainCardBtn = document.getElementById('obtain-card');
const addToDeckBtn = document.getElementById('add-to-deck');
const cardIdInput = document.getElementById('card-id-input');

const battleBtn = document.getElementById('battle-button');
const card1Input = document.getElementById('card1-id');
const card2Input = document.getElementById('card2-id');
const battleResultEl = document.getElementById('battle-result');

const drawCardBtn = document.getElementById('draw-card-button');

function renderCards(container, cards) {
    container.innerHTML = '';
    cards.forEach(card => {
        const div = document.createElement('div');
        div.className = 'card';
        div.innerHTML = `
            <h4>${card.name}</h4>
            <p>ID: ${card.id}</p>
            <p>Faction: ${card.faction}</p>
            <p>ATK: ${card.attack} | HP: ${card.health}</p>
        `;
        container.appendChild(div);
    });
}

function updateUI() {
    renderCards(inventoryEl, player.inventory);
    renderCards(deckEl, player.deck);
    renderCards(handEl, player.hand);
}

obtainCardBtn.addEventListener('click', () => {
    const newCard = getRandomLibraryCard();
    player.addCardToInventory(newCard);
    updateUI();
});

addToDeckBtn.addEventListener('click', () => {
    const cardId = cardIdInput.value.trim();
    if (cardId) {
        player.addCardToDeck(cardId);
        cardIdInput.value = '';
        updateUI();
    }
});

drawCardBtn.addEventListener('click', () => {
    const drawn = player.drawCard();
    if (!drawn) {
        alert("No more cards in the deck to draw!");
    }
    updateUI();
});

battleBtn.addEventListener('click', () => {
    const c1Id = card1Input.value.trim();
    const c2Id = card2Input.value.trim();
    const c1 = player.getCardFromDeck(c1Id);
    const c2 = player.getCardFromDeck(c2Id);

    if (!c1 || !c2) {
        battleResultEl.textContent = "Both cards must be from your deck.";
        return;
    }

    const result = battleCards(c1, c2);
    battleResultEl.textContent = result;
});

// Initialize UI
updateUI();
