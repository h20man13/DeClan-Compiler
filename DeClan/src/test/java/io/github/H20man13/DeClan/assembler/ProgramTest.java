package io.github.H20man13.DeClan.assembler;

import org.junit.Test;

public class ProgramTest {
    @Test
    public void testWriteIntProgram(){
        String assembly = "B begin_0\r\n" + //
                          "h: .WORD 0\r\n" + //
                          "i: .WORD 10\r\n" + //
                          "begin_0: B begin_1\r\n" + //
                          "WriteLn: LDR R2, [R14]\r\n" + //
                          "SUB R14, R14, #2\r\n" + //
                          "WriteInt: LDR R3, [R14]\r\n" + //
                          "SUB R14, R14, #2\r\n" + //
                          "WriteReal: LDR R2, [R14]\r\n" + //
                          "SUB R14, R14, #2\r\n" + //
                          "ReadInt: LDR R3, [R14]\r\n" + //
                          "SUB R14, R14, #2\r\n" + //
                          "begin_1: LDR R2, i\r\n" + //
                          "STR R2, j\r\n" + //
                          "B begin_2\r\n" + //
                          "begin_2: ADD R14, R14, #3\r\n" + //
                          "LDR R3, j\r\n" + //
                          "LDR R3, c\r\n" + //
                          "STR R3, [R14,-R3]\r\n" + //
                          "BL WriteInt\r\n" + //
                          "STP\r\n";
        String expectedOutput = "11101010000000000000000000001100\r\n" + //
                                "00000000000000000000000000000000\r\n" + //
                                "00000000000000000000000000001010\r\n" + //
                                "11101010000000000000000000110000\r\n" + //
                                "11100101000111100010000000000000\r\n" + //
                                "11100010010011101110000000000010\r\n" + //
                                "11100101000111100011000000000000\r\n" + //
                                "11100010010011101110000000000010\r\n" + //
                                "11100101000111100010000000000000\r\n" + //
                                "11100010010011101110000000000010\r\n" + //
                                "11100101000111100011000000000000\r\n" + //
                                "11100010010011101110000000000010\r\n" + //
                                "11100101000111110010000000101100\r\n" + //
                                "11100101000011110010000000111000\r\n" + //
                                "11101010000000000000000000111100\r\n" + //
                                "11100010100011101110000000000011\r\n" + //
                                "11100101000111110011000001000100\r\n" + //
                                "11100101000111110011000001001000\r\n" + //
                                "11100111000011100011000000000011\r\n" + //
                                "11101011000000000000000000011000\r\n" + //
                                "00000110000000000000000000010000\r\n";
        TestUtils.testAgainstInput(assembly, expectedOutput);
    }
}
