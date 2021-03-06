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
package org.nuunframework.kernel.internal.scanner;

import java.net.URL;
import java.util.Set;


public class ClasspathScannerFactory
{
    public ClasspathScanner create(ClasspathStrategy classpathStrategy, Set<URL> additionalClasspath , String... packageRoot)
    {
        ClasspathScannerInternal classpathScannerInternal = new ClasspathScannerInternal(classpathStrategy, packageRoot);
        classpathScannerInternal.setAdditionalClasspath(additionalClasspath);
        return classpathScannerInternal;
    }

    public ClasspathScanner create(ClasspathStrategy classpathStrategy, String... packageRoot)
    {
        return new ClasspathScannerInternal(classpathStrategy, packageRoot);
    }

    public ClasspathScanner create(ClasspathStrategy classpathStrategy, boolean reachAbstractClass ,  String packageRoot,String... packageRoots )
    {
        return new ClasspathScannerInternal(classpathStrategy, reachAbstractClass , packageRoot ,packageRoots);
    }

}
