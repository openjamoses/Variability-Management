/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.variability.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author john
 */
public class MUMTest {

    public static void main(String[] args) {
        // int[] a = {7, 15, 2, 3};
        //System.out.println(isBalanced(a));;

        //System.out.println(evenNatured(1000200004));;
        //System.out.println(gcd(14, 8, 4));
        //mutiple();
        //sort_();
        int[] num1 = {1,5,20,11,16};
        int[] num2 = {5,10,17};
        int[] res = solution(num1, num2);
        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }
        
    }

    static int isBalanced(int[] a) {
        int flag = 0, flag1 = 0, flag2 = 0, flag3 = 0;
        for (int i = 0; i < a.length; i++) {
            if (i % 2 == 0) {
                if (a[i] % 2 == 0) {
                    flag++;
                } else {
                    flag1++;
                }
            }
            if (i % 2 != 0) {
                if (a[i] % 2 != 0) {
                    flag2++;
                } else {
                    flag3++;
                }

            }
        }
        if (flag > 0 && flag1 == 0 && flag2 > 0 && flag3 == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    static int evenNatured(int n) {
        int sum = 0;
        while (n != 0) {
            int digit = n % 10;
            sum += digit;
            n /= 10;
        }

        if (sum % 2 == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    static int gcd(int a, int b, int c) {
        int gch = 1;
        List<Integer> fac_a = new ArrayList<>();
        List<Integer> fac_b = new ArrayList<>();
        List<Integer> fac_c = new ArrayList<>();
        for (int i = 1; i <= a; i++) {
            if (i % a == 0) {
                fac_a.add(i);
            }
        }
        for (int i = 1; i <= b; i++) {
            if (i % b == 0) {
                fac_b.add(i);
            }
        }
        for (int i = 1; i <= c; i++) {
            if (i % c == 0) {
                fac_c.add(i);
            }
        }

        for (int i = fac_a.size() - 1; i >= 0; i--) {
            if (fac_b.contains(fac_a.get(i)) && fac_c.contains(fac_a.get(i))) {
                gch = fac_a.get(i);
                break;
            }
        }
        return gch;
    }

    private static void mutiple() {
        int sum = 0;
        for (int i = 1; i < 251; i++) {
            if (i % 9 == 0) {
                sum += i;
                System.out.println(i + "");
            }
        }

        System.out.println("Sum: " + sum);
    }

    private static void sort_() {
        String[] inputs = {"Abia", "Adamawa", "Anambra", "Akwa Ibom", "Bauchi", "Bayelsa", "Benue", "Borno", "Cross River", "Delta", "Ebonyi", "Enugu", "Edo", "Ekiti", "Gombe", "Imo", "Jigawa", "Kaduna", "Kano", "Katsina", "Kebbi", "Kogi", "Kwara", "Lagos", "Nasarawa", "Niger", "Ogun", "Ondo", "Osun", "Oyo", "Plateau", "Rivers", "Sokoto", "Taraba", "Yobe", "Zamfara"};
        List<Integer> lenths = new ArrayList<>();
        List<String> lists = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++) {
            lenths.add(inputs[i].length());
            lists.add(inputs[i]);
        }

        insertion(lists, lenths);
        for (int i = lists.size() - 1; i >= 0; i--) {
            System.out.println(i + " : " + lists.get(i));
        }

    }

    private static void insertion(List<String> lists, List<Integer> inputs) {
        int temp;
        String temp2;
        String tit;
        List<String> name;
        for (int i = 1; i < inputs.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (inputs.get(j) < inputs.get(j - 1)) {
                    temp = inputs.get(j);
                    inputs.set(j, inputs.get(j - 1));
                    inputs.set(j - 1, temp);

                    temp2 = lists.get(j);
                    lists.set(j, lists.get(j - 1));
                    lists.set(j - 1, temp2);

                }
            }
        }
    }

    int[] solution(int D, int[] A) {
        // write your code in Java SE 8
        List<Integer> res = new ArrayList<>();
        int flag = -1;
        if (A.length > 0) {
            if (A[0] == -1) {

            }
            for (int i = 0; i < A.length; i++) {
                if (A[i] == D) {
                    flag = i;
                }
            }
            if (flag != -1) {

            }

        }
        return null;
    }

    static int[] solution(int[] stores, int[] houses) {
        // write your code in Java SE 8
        int[] num = new int[houses.length];
        for (int a = 0; a < houses.length; a++) {
            int[] diff = new int[stores.length];
            int[] tempStore = new int[stores.length];
            for (int i = 0; i < houses.length; i++) {
                for (int j = 0; j < stores.length; j++) {
                    diff[j] = houses[i] - stores[j];
                }
            }
            
            for (int j = 0; j < stores.length; j++) {
               
                for (int i = 0; i < diff.length; i++) {
                   
                }
            }
           
            num[a] = tempStore[tempStore.length-1];
            
        }
        return num;
    }
}
