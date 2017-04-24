metadata {
definition (name: "Somfy Blind", namespace: "chriswood1001", author: "Chris Wood") {
capability "Switch"
capability "Switch Level"
capability "Refresh"
fingerprint deviceId: "0x1105", inClusters: "0x2C, 0x72, 0x26, 0x20, 0x25, 0x2B, 0x86"

}
simulator {
// status messages
}
tiles {
standardTile("switch", "device.switch", width: 2, height: 2, icon: "st.Home.home9", canChangeIcon: true) {
state "open", label:'open', action:"switch.off", icon:"st.Home.home9", backgroundColor:"#79b821", nextState:"closedd"
state "closedd", label:"closed", action:"refresh.refresh", icon:"st.Home.home9", backgroundColor:"#ffffff", nextState:"open"
state "closedu", label:"closed", action:"refresh.refresh", icon:"st.Home.home9", backgroundColor:"#ffffff", nextState:"open"
}
main "switch"
details(["switch"])
}}

def parse(String description) {
def result = null
def cmd = zwave.parse(description)
if (cmd) {
result = zwaveEvent(cmd)
log.debug "Parsed ${cmd} to ${result.inspect()}"
} else {
log.debug "Non-parsed event: ${description}"
}
return result
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
}
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
[name: "switch", value: cmd.value ? "on" : "off", type: "physical"]
}
def zwaveEvent(physicalgraph.zwave.Command cmd) {
return createEvent(descriptionText: "${device.displayName}: ${cmd}")
}
def on() {
log.debug "Closing blinds in the 'up' position"
[name: "switch", value: "closedu", type: "physical"]
return zwave.switchMultilevelV1.switchMultilevelSet(value: 0xFF).format()
}
def off() {
log.debug "Closing blinds in the 'down' position"
[name: "switch", value: "closedd", type: "physical"]
return zwave.switchMultilevelV1.switchMultilevelSet(value: 0x00).format()
}
def refresh() {
log.debug "Opening blinds to 'my' position"
[name: "switch", value: "open", type: "physical"]
return zwave.switchMultilevelV1.switchMultilevelStopLevelChange().format()
}