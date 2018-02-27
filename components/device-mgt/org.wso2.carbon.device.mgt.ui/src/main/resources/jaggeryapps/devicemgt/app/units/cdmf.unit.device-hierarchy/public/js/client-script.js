var graph = null;
var backendApiUrlNodes = "/api/device-mgt/v1.0/device-hierarchy/1.0.0/graph";
var successCallbackNodes = function (dataGraph) {
    if (dataGraph) {
        console.log(JSON.parse(dataGraph));
        graph = JSON.parse(dataGraph);
        console.log(graph);
    }
};
invokerUtil.get(backendApiUrlNodes, successCallbackNodes, function (messageGraph) {
});

var container = document.getElementById('mynetwork');
var data = {
    nodes: nodes,
    edges: edges
};
var options = {
    layout: {
        randomSeed: 100
    },
    nodes: {
        shape: 'dot',
        font: {
            size: 15,
            color: '#ffffff'
        },
        borderWidth: 2
    },
    edges: {
        width: 2,
        color: '#ffff00',
    },
    interaction: {
        dragNodes: false
    }
};
network = new vis.Network(container, data, options);
network = new vis.Network(container, data, options);
network.once("beforeDrawing", function() {
    network.focus("server", {
        scale: 33
    });
});
network.once("afterDrawing", function() {
    network.fit({
        animation: {
            duration: 3000,
            easingFunction: "easeInOutCubic"
        }
    });
});
