/*
 * Copyright 2013-2022 smartics, Kronseder & Reiner GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.de.smartics.maven.plugin.jboss.modules.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.Test;

import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToModule;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;

/**
 * Tests {@link de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlParser}
 * with <code>{@value #ID}</code>.
 */
public class ModulesXmlParserApplyToModuleXmlTest extends
    AbstractModulesXmlParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  private static final String ID = "apply-to-module.xml";

  // --- members --------------------------------------------------------------

  // ****************************** Constructors ******************************

  public ModulesXmlParserApplyToModuleXmlTest()
  {
    super(ID);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  // --- helper ---------------------------------------------------------------

  // --- tests ----------------------------------------------------------------

  @Test
  public void parsesXml() throws Exception
  {
    assertThat(result.getModulesId(), is(equalTo(ID)));

    final List<ModuleDescriptor> descriptors = result.getDescriptors();
    assertThat(descriptors.size(), is(equalTo(1)));
    final ModuleDescriptor descriptor = descriptors.get(0);
    assertThat(descriptor.getName(), is(equalTo("apply-to-modules")));
    assertThat(descriptor.getSlot(), is(nullValue()));

    final ArtifactMatcher matcher = descriptor.getMatcher();
    final List<ArtifactClusion> includes = matcher.getIncludes();
    assertThat(includes.size(), is(equalTo(1)));
    final ArtifactClusion include1 = includes.get(0);
    assertThat(include1.getGroupId(), is(equalTo("de.smartics.test")));
    assertThat(include1.getArtifactId(), is(equalTo("service-test")));

    assertApplyToModule(descriptor);
  }

  private void assertApplyToModule(final ModuleDescriptor descriptor)
  {
    final ApplyToModule apply = descriptor.getApplyToModule();
    final String mainClassXml = apply.getMainClassXml();
    assertThat(
        mainClassXml,
        is(equalTo("<main-class xmlns=\"http://smartics.de/ns/jboss-modules-descriptor/1\" name=\"de.smartics.test.Main\" />")));
    final List<String> dependenciesXmls = apply.getDependenciesXml();
    assertThat(dependenciesXmls.size(), is(equalTo(2)));
    assertThat(
        dependenciesXmls.get(0),
        is(equalTo("<module xmlns=\"http://smartics.de/ns/jboss-modules-descriptor/1\" name=\"javax.api\" />")));
    assertThat(
        dependenciesXmls.get(1),
        is(equalTo("<module xmlns=\"http://smartics.de/ns/jboss-modules-descriptor/1\" name=\"javax.xml.stream.api\">"
                   + "<imports><exclude-set><path name=\"org.jboss.example.tests\" />"
                   + "</exclude-set></imports></module>")));
    final String exportsXml = apply.getExportsXml();
    assertThat(
        exportsXml,
        is(equalTo("<exports xmlns=\"http://smartics.de/ns/jboss-modules-descriptor/1\"><exclude path=\"**/impl/*\" /></exports>")));
  }
}
