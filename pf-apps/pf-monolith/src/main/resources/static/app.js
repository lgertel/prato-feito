var stompClient = null;

function setConnected(connected) {
  document.getElementById('connect').disabled = connected;
  document.getElementById('disconnect').disabled = !connected;
  document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
  document.getElementById('response').innerHTML = '';
}

function connect() {
  stompClient = Stomp.client('ws://localhost:8080/pf/websocket/');
  stompClient.debug = null;
  stompClient.connect({}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);

    stompClient.subscribe("/topic/customers.updates", function (message) {
      alert(message.body);
    });
  });
}

function disconnect() {
  if (stompClient != null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function sendCommand() {
  var firstName = document.getElementById('firstName').value;
  var lastName = document.getElementById('lastName').value;
  var orderLimit = document.getElementById('orderLimit').value;

  stompClient.send("/app/customers/createcommand", {}, JSON.stringify({
    'firstName': firstName,
    'lastName': lastName,
    'orderLimit': orderLimit
  }));
}

function createRestaurantCommand() {
  alert('creating restaurant')
  stompClient.send("/app/restaurants/createcommand", {}, JSON.stringify({
    'name': 'Restaurante do Gordo',
    'menuItems': [{'id': '1234', 'name': 'Rabada com Mandioca', 'price': '25'}]
  }));
}
