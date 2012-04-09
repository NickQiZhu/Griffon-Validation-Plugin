/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * @author nick.zhu
 */

//def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
//eventCopyLibsEnd = { jardir ->
//    eventClosure1(jardir)
//    if (!isPluginProject) {
//        def pluginDir = getPluginDirForName('validation')
//        if(pluginDir?.file?.exists()) {
//            ant.fileset(dir: "${pluginDir.file}/lib/", includes: '*.jar').each {
//                griffonCopyDist(it.toString(), jardir)
//            }
//        }
//    }
//}

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('validation')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-validation-plugin', dirs: "${validationPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('validation', [
        conf: 'compile',
        name: 'griffon-validation-addon',
        group: 'org.codehaus.griffon.plugins',
        version: validationPluginVersion
    ])
}

eventCollectArtifacts = { artifactsInfo ->
    if(!artifactsInfo.find{ it.type == 'constraint' }) {
        artifactsInfo << [type: 'constraint', path: 'constraints', suffix: 'Constraint']
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == 'constraints'} ) {
        pathToInfo << [name: 'Constraints', path: 'constraints', filetype: ['.groovy','.java']]
    }
}

