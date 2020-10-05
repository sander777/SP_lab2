package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Main {
    public static class Transition {
        public int from;
        public int to;
        public char a;

        public Transition(String s) {
            String[] in = s.split(" ");
            this.from = Integer.parseInt(in[0].trim());
            this.to = Integer.parseInt(in[2].trim());
            this.a = in[1].trim().toCharArray()[0];
        }
    }

    public static class StateMachine {
        public HashSet<Character> A;
        public HashSet<Integer> S;
        public int s_0;
        public HashSet<Integer> F;
        public HashSet<Transition> T;

        public StateMachine(String in) {
            this.A = new HashSet<>();
            String[] inputs = in.split("\n");
            for (int i = 0; i < Integer.parseInt(inputs[0].trim()); i++) {
               this.A.add((char)(97 + i));
            }
            this.S = new HashSet<>();
            for (int i = 0; i <= Integer.parseInt(inputs[1].trim()); i++) {
                this.S.add(i);
            }
            this.s_0 = Integer.parseInt(inputs[2].trim());
            this.F = new HashSet<>();
            String[] fStates = inputs[3].split(" ");
            for(int i = 1; i < fStates.length; i++) {
                this.F.add(i);
            }
            this.T = new HashSet<>();
            for (int i = 5; i < inputs.length; i++) {
                this.T.add(new Transition(inputs[i]));
            }
        }

        public HashSet<Integer> getReachableStates() {
            HashSet<Integer> reachableStates = new HashSet<>();
            reachableStates.add(s_0);
            while (true) {
                int lengthBefore = reachableStates.size();

                for (Transition t : this.T) {
                    if (reachableStates.contains(t.from)) {
                        reachableStates.add(t.to);
                    }
                }

                int lengthAfter = reachableStates.size();
                if (lengthBefore == lengthAfter) break;
            }

            return reachableStates;
        }

        public HashSet<Integer> getUnreachableStates() {
            HashSet<Integer> diff = (HashSet<Integer>) this.S.clone();
            diff.removeAll(this.getReachableStates());
            return diff;
        }

        public HashSet<Integer> getDeadEnds() {
            HashSet<Integer> reachable = this.getReachableStates();
            for (Transition t : this.T) {
                reachable.remove(t.from);
            }
            return reachable;
        }
    }

    public static void main(String[] args) {
        try {
            StateMachine stateMachine = new StateMachine(Files.readString(Path.of("state_machine.txt")));
            System.out.println("Unreachable states: " + stateMachine.getUnreachableStates());
            System.out.println("Dead ends: " + stateMachine.getDeadEnds());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}