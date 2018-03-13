from icu import Normalizer2

composer = Normalizer2.getNFCInstance()
decomposer = Normalizer2.getNFDInstance()

def compDecomp(orig):
    composed = composer.normalize(orig)
    decomposed = decomposer.normalize(orig)
    print(f"{orig} {composed} {decomposed}")

compDecomp('lội')
