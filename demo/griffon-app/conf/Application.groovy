application {
	title="Demo"
	startupGroups=["demo"]
	autoShutdown=true
}
mvcGroups {
    // MVC Group for "Dialog"
    'Dialog' {
        model = 'demo.DialogModel'
        view = 'demo.DialogView'
        controller = 'demo.DialogController'
    }

	demo {
        model="DemoModel"
        view="DemoView"
        controller="DemoController"
    }
}
