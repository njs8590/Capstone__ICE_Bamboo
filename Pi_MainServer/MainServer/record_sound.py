from pydub import AudioSegment
from pydub.playback import play

song = AudioSegment.from_wav("micTest.wav")
play(song)
