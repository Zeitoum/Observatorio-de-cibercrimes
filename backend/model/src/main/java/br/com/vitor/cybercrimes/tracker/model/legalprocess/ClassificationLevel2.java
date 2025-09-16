package br.com.vitor.cybercrimes.tracker.model.legalprocess;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel1.*;

@Getter
@RequiredArgsConstructor
public enum ClassificationLevel2 {
    A1(A),
    A2(A),
    A3(A),
    A4(A),
    A5(A),
    A6(A),
    B1(A),
    B2(B),
    B3(B),
    C1(C),
    C2(C),
    C3(C),
    C4(C),
    C5(C),
    C6(C),
    C7(C),
    D1(D),
    D2(D),
    E1(E),
    E2(E),
    E3(E),
    E4(E);

    private final ClassificationLevel1 classificationLevel1;
}