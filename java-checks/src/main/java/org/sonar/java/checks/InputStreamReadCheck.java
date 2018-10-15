/*
 * SonarQube Java
 * Copyright (C) 2012-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks;

import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.java.matcher.TypeCriteria;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "S4517")
public class InputStreamReadCheck extends IssuableSubscriptionVisitor {

  private static final MethodMatcher INPUT_STREAM_READ = MethodMatcher.create().typeDefinition(TypeCriteria.subtypeOf("java.io.InputStream")).name("read").withAnyParameters();

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.METHOD);
  }

  @Override
  public void visitNode(Tree tree) {
    if (!hasSemantic()) {
      return;
    }
    MethodTree method = (MethodTree) tree;
    if (!INPUT_STREAM_READ.matches(method)) {
      return;
    }
    Symbol.MethodSymbol overriddenSymbol = method.symbol().overriddenSymbol();
    if (overriddenSymbol == null || overriddenSymbol.isUnknown()) {
      // unable to tell it's an override
      return;
    }
    if (!overriddenSymbol.owner().type().is("java.io.InputStream")) {
      // not one of the original "read()"
      return;
    }
    // TODO
  }
}