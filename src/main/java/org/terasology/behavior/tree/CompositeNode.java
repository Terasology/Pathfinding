/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.behavior.tree;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author synopia
 */
public abstract class CompositeNode extends Node {
    private final List<Node> children = Lists.newArrayList();

    public List<Node> children() {
        return children;
    }

    @Override
    public int maxChildren() {
        return Integer.MAX_VALUE;
    }

    public abstract static class CompositeTask extends Task {
        protected CompositeTask(Node node) {
            super(node);
        }

        @Override
        public CompositeNode getNode() {
            return (CompositeNode) super.getNode();
        }
    }
}