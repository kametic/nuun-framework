/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuunframework.kernel.tests.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.runner.RunWith;
import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.tests.NuunITRunner;
import org.nuunframework.kernel.tests.annotations.ITBind;


/**
 * @author epo.jemba@kametic.com
 *
 */
public class NuunITPlugin extends AbstractPlugin
{

    private Collection<Class<?>> integrationTestsClass;

    @SuppressWarnings("unchecked")
    private final Specification<Class<?>> iTSpecs = or(classAnnotatedWith(RunWith.class), classAnnotatedWith(ITBind.class));

    @Override
    public String name() {
        return "nuun-kernel-tests-plugin";
    }

    @Override
    @SuppressWarnings("rawtypes")
    public InitState init(InitContext initContext) {
        Map<Specification, Collection<Class<?>>> scannedTypesBySpecification = initContext.scannedTypesBySpecification();
        Collection<Class<?>> iTClassCandidates = scannedTypesBySpecification.get(iTSpecs);
        if (iTClassCandidates != null && !iTClassCandidates.isEmpty()) {
            integrationTestsClass = new ArrayList<Class<?>>();
            for (Class<?> itCandidate : iTClassCandidates) {
                if ((itCandidate.getAnnotation(RunWith.class) != null &&                         
//                        itCandidate.getAnnotation(RunWith.class).value().equals(NuunITRunner.class) 
                        NuunITRunner.class.isAssignableFrom( itCandidate.getAnnotation(RunWith.class).value())
                        )
                        || itCandidate.getAnnotation(ITBind.class) != null) {
                    integrationTestsClass.add(itCandidate);
                }
            }
        }
        return InitState.INITIALIZED;
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder().specification(iTSpecs).build();
    }

    @Override
    public Object dependencyInjectionDef() {
        return new NuunITModule(integrationTestsClass);
    }

}
