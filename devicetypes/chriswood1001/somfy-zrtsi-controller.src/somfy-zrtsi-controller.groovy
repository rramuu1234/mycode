metadata {
definition (name: "Somfy ZRTSI Controller", namespace: "chriswood1001", author: "Chris Wood") {
fingerprint deviceId: "0x0200", inClusters: "0x86, 0x72"
}
simulator {
}
tiles {
standardTile("state", "device.state", width: 2, height: 2) {
state 'connected', icon: "st.unknown.zwave.static-controller", backgroundColor:"#ffffff"
}
main "state"
details(["state"])
}
}


def parse(String description) {
def result = null
if (description.startsWith("Err")) {
result = createEvent(descriptionText:description, displayed:true)
} else {
def cmd = zwave.parse(description)
if (cmd) {
result = createEvent(zwaveEvent(cmd))
}
}
return result
}



def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
def event = [displayed: true]
event.linkText = device.label ?: device.name
event.descriptionText = "$event.linkText: ${cmd.encapsulatedCommand()} [secure]"
event
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
def event = [displayed: true]
event.linkText = device.label ?: device.name
event.descriptionText = "$event.linkText: $cmd"
event
}