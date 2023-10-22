package com.valkiria.uicomponents.components.signature

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.extensions.decodeAsBase64Bitmap

@Composable
fun CrewMemberSignatureComponent(
    uiModel: CrewMemberSignatureUiModel
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        Column(
            modifier = uiModel.modifier
        ) {
            Text(
                text = uiModel.name.text,
                style = uiModel.name.textStyle.toTextStyle()
            )

            Text(
                text = uiModel.identification.text,
                style = uiModel.identification.textStyle.toTextStyle()
            )

            Image(
                bitmap = uiModel.signature.substringAfter(",").decodeAsBase64Bitmap()
                    .asImageBitmap(), // FIXME: Temporal hack
                contentDescription = uiModel.name.text,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true)
@Composable
fun CrewMemberSignatureComponentPreview() {
    CrewMemberSignatureComponent(
        uiModel = CrewMemberSignatureUiModel(
            identifier = "slider",
            name = TextUiModel(
                text = "Andres Barragán Rojas",
                textStyle = TextStyle.HEADLINE_5
            ),
            identification = TextUiModel(
                text = "CC 1070926476",
                textStyle = TextStyle.HEADLINE_5
            ),
            signature = """/9j/4AAQSkZJRgABAQEAeAB4AAD/4gHbSUNDX1BST0ZJTEUAAQEAAAHLAAAAAAJAAABtbnRyU
                |kdCIFhZWiAAAAAAAAAAAAAAAABhY3NwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAA9tYAAQAAAADT
                |LVF0BQ8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlyWFlaAAAA8AA
                |AABRnWFlaAAABBAAAABRiWFlaAAABGAAAABR3dHB0AAABLAAAABRjcHJ0AAABQAAAAAxyVFJDAAABTAAAAC
                |BnVFJDAAABTAAAACBiVFJDAAABTAAAACBkZXNjAAABbAAAAF9YWVogAAAAAAAAb58AADj0AAADkVhZWiAAA
                |AAAAABilgAAt4cAABjcWFlaIAAAAAAAACShAAAPhQAAttNYWVogAAAAAAAA808AAQAAAAEWwnRleHQAAAAA
                |Ti9BAHBhcmEAAAAAAAMAAAACZmYAAPKnAAANWQAAE9AAAApbZGVzYwAAAAAAAAAFc1JHQgAAAAAAAAAAAAA
                |AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                |AAAAAAAAD/2wCEAAQEBAQEBAUFBQUHBwYHBwoJCAgJCg8KCwoLCg8WDhAODhAOFhQYExITGBQjHBgYHCMpI
                |iAiKTEsLDE+Oz5RUW0BBAQEBAQEBQUFBQcHBgcHCgkICAkKDwoLCgsKDxYOEA4OEA4WFBgTEhMYFCMcGBgc
                |IykiICIpMSwsMT47PlFRbf/CABEIAMkBlQMBEQACEQEDEQH/xAAwAAEAAwEBAQEAAAAAAAAAAAAAAQMFAgQ
                |GCAEBAQEBAAAAAAAAAAAAAAAAAAECA//aAAwDAQACEAMQAAAA/fwAAAAAAAAAAB57Is9OdAAAAAAAAAAAAA
                |AAAAAAAAAACD53pz38bslAAAAAAAAAAAAAAAAAAAAAAAGfrObrP0XPoAAAAAAAAAAAAAAAAAAAAAAAIMHeP
                |UauNgAAAAAAAAAAAAAAAAAAAAAADM3ik2M6mUAAAAAAAAAAAAAAAAAAAAAADmzO1KE2sbAAAAAAA5syNZtN
                |PG5AAAAAAAAAAKrLRKABBjbxpy8HqzoAAAAAAcWYus7eNzZk6zr42AAAAAAAAABj7xr43IAIMfeNjOsnWdb
                |G5AAAAAAB89056Wde/Ohk7xq43IAAAAAAAABCZ+86ONgDky940867M3U0saAAAAAAGZvHBq51Mo8Gs+6WZQ
                |AAAAAAAAIMneNfGwIsxtZ2M66lztZ0JZlAAAAAAizOsps1sb6Bl7xp43IAAAAAAAAAMneKz3S+SyyXSzqSD
                |I3jYxsAAAAAAZO8audZes6uNiDI3jYxsAAAAAAAAAAeTWc+waMvplHlsvl6K7Ls6AAAAAgyt4tl4s08bizG
                |1nbzpKAAAAAAAAAAAIFnms81ngs9EvRRZr516JUo8Ws+3OgIKtZ7lHnsyNZ6jo9S6eNyBZEsgAg8Os+/OgA
                |AAAAAABFnks9mdQcWYe8a2derOh8/vnoTXvzpWJrGxLWZNnsWpNbOs+zRlSwZO8WnJBq42Bk7xq51MoAAAA
                |EAEgAyd41cbkA5sx9Z0pYPOesy7INrOplAx941s66lgxt41JcmzbzqZQsxdZ1prxpeenOgAAAPHrOfZAByX
                |Ak6PJZ783teiw7B4zo98qUCQAYu8bONyCCrWbc6kAAo1m2XqUAAAAAAAADzaz0XZ0sEnJVZwVlVlZyQdlpb
                |L0eGzYzpLIAAAAAAsEAmUAAAAADJ3jVxuQAAAAAQBZl6zbKKbOCS6PQt8vQlkAAHl1mk4s5OCADs7i1eywt
                |lkk5lyN42cbAAAAAAAGNvGxjcgAgWUWeYosg98vqzoAAAAAQBYIKrKys89nnq6B0WFstpbLYQJZAAIMveNX
                |GwAAAAOToAAAAAAAAAHFldl+dCALJKimyoqsrBJaXS8HKaU1EokAAAAAAAAAAAAAAo1m2XqUAAAAAQBZl6z
                |dL0UldnIBYamNyAAAAAAAAAAADz6zfLMoAAAAAAGfrOhnQAAEEgAAAAAAAAAAAFViy3OgAAAAAAAAAAAAAA
                |AAAAAAAAAIs8dntzoAAAAAAAAAAAAAAAAAAAAAAAAU2XSgAAAAAAAAAAAAAAAf/EADsQAAIBAgIHAwoGAgI
                |DAAAAAAECAwAEBRESEyExQVGRInGBECAwMkBCQ1JhoRQVUFNysSPBNGKCkuH/2gAIAQEAAT8A9nvrzVDVRH
                |bxNYZPK7SLI5YAZ7dv6jNKIY2c0zF2LE5k1hUWjAzn3z9h+o4pPpyiMHYm/voKWYKN5OVIoRFUbgMv1CaUQ
                |xs5pc3ZnbbltPfWHxh59NtybfH9RxC518ugvqr9zU6aiNIvePaf/QrDodVbgkbX2/qGIXIhj0FPab7CrCAM
                |xmk9RNveaGleXW3326D2C7xBICUTtPx5Cjd3bnY57hUWI3ERyY6Q5GopUmTSU+zXWJCFzHGoYjeTuq2nNxF
                |pkZHPLzpJFiQu24Uolvbj6segq/ZLa1WGPZpbPAVhMPrzHuHp7+81C6uM9sjoKtotfOiHidtKqoMlAAq4tk
                |nU5jJuBqzma2uQG3E6LD2V20UZuQJqyhFxcAPtGRJoKFGQGQ80kKCScgKuJ3uZAFGzcoq0thbpt9c7zV7Kb
                |i5OW4dlaiQRxqg4D00sqwxlzTF5WZz3msKj0pnf5R9z5cRQR3TZDLMA1G2nGjcwD7JOM4JP4msKIFy31Q+a
                |zKilmOQFXd490+ioIQHYOdWNkIBrJPX5cqupdTCzcdw7zWGQ6yfTO5Bn4+nxC5M8ugvqr9zVzF+HhjjPrt2
                |m/wBCsMj0LbSI2uc/Li2WvT+FWv8Ax4v4+yMMwRzFWD6u7TP6jzLi+ig2A6T8hTyXF7KBvPADcKtLFIBpvt
                |f+vJik+smEY3J/Zqzi1MC8ztPpryfUQkj1m2CsNt9dLrGHZT7mrxzPdvlz0RSqEUKNwGXlxJ9O6I+UAVGuh
                |Gi8lA9lvYWt7gsNgY6SmmvbpjtlPhUeKlYgGUs/PdU17c3B0c8gfdWocOlcaUnZH3qONIlyQZDySOI0ZjwF
                |WsbXNyM9u3Sb099Jrbgqu5dgpVFnZnmFzPeaw+PW3IJ3LtPlmlWFCzVbRtdXIz56TezOiyKVYZip8KAzaJ/
                |Bqks7mL1ozlzG2oLqW3PZC+IpMX+eLoaXErVt5I7xSzwtukXrWKTqQkSH6msLg0ITIRtfd3Cr4yJbEoSMiM
                |8uVYXcNIrRuSSNo7vSSPoIzchVhFrrpSdoXtGr+9E2cSDsA7+dWF3HbFg6+t7w4UCGAIOYNT3cUAyJzbkKZ
                |pryUDLM8BwFW1utumW9jvPtLRo/rKDT4batuBXuNNhHyy9RUmGXCAnNCO+lRnYKoJPIVp3kI0c5FA4HOjfX
                |JQozBgdhBFQytBIrrvFLi6bNKIjuOdR39vIwUEgnmPK2ISR3JXIaCtkR5jyJGM3YAU2LRg9mNiOZ2VHikDb
                |GDLV/MjWZ0CCGI3VhSZiVu4Z040WZeRIp3RgNFNEgbdu+knmRCiuQpqDD55jpP2V5nfUUMcK5IPK1xApyMi
                |0k0TnJXBPmzSrDGXPhUOIzgMNWG7uHseIzaq3Kg7X2eFYVDmzzHhsHkKq28A1eWEbxloUCuNuQ40mgHykBy
                |+m8VFh0JKyLNpLv8r5vK+QzzY1Y3+pyjl9TgeVAgjMVNMsKFm8BzrKa7l5n7Co8KgC9tmY9BUuEqdsTkHk1
                |SwzQHRcEf0atLqOC2lXM6w7RVjCs03b2qBma/A2v7Y6msRtBCyugyQ7MuRrDrnXQ6DHtJ9x5JJEiUsxyFXF
                |3NdPojMLwUVHhdwwzYqv0NS4bcxjMZMPpVvfTW50W7S8jvFRypKukhz8t7cm6myX1RsUc6tYBBGB7x3+i00
                |HvDrWui/cXqK10P7idRWvg/dTqKWSNjkrqT9D5t/JrLgqu5dgqCPVRKnIbfMvLBJgXTJX+xpXubNyNqniOB
                |qPFlOWsjI+oq4xONoysatmeJrDIC82sI7Kf2au8NDkvDkG4rwNJNdWhKglfoRRM95JzP2FW8CwJojfxPldF
                |dSrDMVe26W0oVGzzGeXKsMi0LfTO9zn4eR0WRCrbjREtjcfVehFNi0WrzVDp8uArOe7k5n7Cra0S3Ge9+J8
                |txaR3Az3PwamW4speR5jcahxSFl/ygq30GYq8xAzDVxZhOJ4msOsiMppB/Ef79Ff2tzJOXVSynLKjaXI+E1
                |fhbn9p+lfhbn9p+lfhrn9l/wD1NCOdDmEcEfQ1rr0e9JX4m8HvvQvbxfiHxAr8xu/n+woOQ4fec86XFLlRl
                |kh8K/NpvkShi8nGJetfm4/ZPWhi0XGNqfELOUZOjEfUCmXDXPZd0qODD882nJ+m6o5LdVCo6AcgaBB3GiAd
                |4oADcPNnY3N02XFshQAUADcPLLDHMui4pcJiB7UjEcqSNI1yQZDzXRXXRYAinwhCexKQORGdQYbDCdJzpkc
                |93smJlI40jUDMnM9wrDLdDC0jqDpHZmOAowQnfGvSja25+GtGxtT8P7mjhlrybrRwq25v1o4RH+61HCNmyb
                |b3UcJl4SLTYXcrxQ+NHD7sbdXn3EVqLxdySeFa28TZpSDvzoX92mzWdQKXFLld4Q+FLi7+9EPA1JiivGyiM
                |gkVZPDFOHlOQA2bM9tC6t23SLQZW3EH0z3EMZyZwDRxC1Hv5+Bo4pa/9ulRXsEzaIJB+vpsQk1t0wG5clFR
                |poRqvIelIB3ijBC2+NelNY2rfD6E1fwQ27oseeZGZzNQYaZ4Vk1miTuGWdPhUygkOhpsOu1Gehn3GtXexbQ
                |JB9RnQvbuP4h8RSYpcLvCmlxfZ2otv0NR4lagAHSHhS3MDbpF/qgQd3nz4ZJJK7q65MSdtflNx86dTRwy5H
                |y9a/Lbv5R1o2N2vwz4EVqLxfckrSvo9mco61+Ku199hQxG7+cHwFLilyvBD3ihi03GNaGLn3oehoYvFxjah
                |ittyfpQxC1Pv5d4NC7tj8Va1seWYdT41bKZ7xM+LaR/v2G6c3F22XFtEUoCqANwHmEA00ELb416U2G2rbgR
                |3GmwhPdlI7xnT4VOozDKaNndxn1G8KFxeRb3cZc//tWd/PLMsbgEHiB6UqDvAowwnfGvSjaWx+EKOH2p9wj
                |uJo4VbHi48aOER8JW6UcIPCbqKOEzcHSprCeBGdtHRHEGoopZSdWpOVam9XcsvhnWvvF3vJ40MQu1GWs6gU
                |uKXK79E94oYvLxjWhi68Yj1pcVtyNquDS4jan3iO8ULq3PxFoOh3MD4+dI+hGzchWHR626BO5c2PpAqg5gA
                |H2XFMhbD6sBWEL/AI5W5sB08pAO8UYYTvjXpRs7Y/DFHDLU8GHjTYTCfVdhTYQfdl6imwqZQSHSjht38oPj
                |X4W7T4beFaV7FszlHWhfXabNYfEUuKXK79E+FLi8nvRKe41dYiLiExhCuZGe2sPuILfTMmeZyyoXtqfiChN
                |C26RetAg7j7di5/xxD/sawoZWx+rn0pANGGFt8a9KNjan4fQmr+3ht3RY89ozOZq3w0TQrIZCpP0zo4RJwl
                |Wmwu5XcVPjRw+7Xbq+hFai9XcknhWtvV3tL450Ly7T4jeIr8yu/nHQVExeNWIyJHtOL+pF3msL/wCL/wCR9
                |gvoLmW6OSEjYFPDKkUIiqOAy8/RXkPasWXOBG5NWEtnC68m/UrxNO3kHEDPpWEMRJIvArn0/U8L+N/IfoX/
                |xAAfEQACAgEFAQEAAAAAAAAAAAAAARESIRAgMDFAAlD/2gAIAQIBAT8A86Q/0Vo/0V+ktH+ikIf6CQ2dLwJ
                |EIhedIajf0hZZ9c6Q9E4HleZ4W9KBuRYXh+tV15UfW5KBuRD50hZY+9fkfflfWxKTCG50Q+ZDYuti68yyiE
                |VISG9jwuddHbH1seF57EoakqQyGIYuxrlfQkNTolJhDc+qWWLaYIWlSHrG2pDF2fWsE7Ie5FfGj61T0l7Gt
                |EpMIsW0aljZLExrVKCxKGtiUDc8cMhkPcuh7E4MMqVGJmGYQ3OxOR969oqYQ3OqcGGQJDfEmiUSiUSiUYIR
                |CIWlUVKlSrIZklmeDpbJgtvsT5PkekslksllixYsiUSjBCKoqVGQ+eGQyGNRzLrwSxORssSjBCKoqQyHwSW
                |LIlEolGCEQiqKlSpVkMh6PC8Kwt0sllixKIQ1zyyWSyxYsTpgwQiqKlSrIZD4H1+Cuz62yyWWZYsSiUYIRV
                |FRKGNEMh+/5H3zyxMbLFkSiUYIRC9XyPvwJqPwvk+v0l2fX6b/AAv/xAAfEQADAAEEAwEAAAAAAAAAAAAAA
                |RESECAhMQIwQFD/2gAIAQMBAT8A+dsT/TX6L/UX6LYxfoNiR2/gbKyv52xO7+2PhHj72xaNUXD+Zcve3RKD
                |5fw+Or7+V9C73N0Sgxe9sfCF1r5C6+Vd7GzliU0Yvc3BIfL2Pv5nwysyK2TYuX72dIXexcv58SMsMioqGIf
                |Qn7V2Nic0bhyxKfVEYmOnJXpkVa3nbkVD6PHWk2VF3X42eOrWk2J6Nw5ZiY6JxCRENCerdMSMT2N0SnrqKi
                |rcxbGqcoyMhDRWjliU2NQXWvTMjliU1apyijYl6mmRkZGRkZyVlZXpkzIyMjIqOCI49Hb2478SfJ5C0iIiI
                |iMTExMWRkZyVmTMjIRV76VFRfc+/giGoJGJGclZkzIqKvRDExZGRkZyVlZkzIyMjJFRVou/hfL3RERiYkZW
                |J++IiIjExMSaRnJWZMyMjJFRV6F3+C+jx2xERijExIyM5KzJmQ2JwqKvv8hde+IaEjExZGRnJWV/V5C6+Bp
                |38LyPH9J9Hj+mvwv/2Q==""".trimMargin(),
            arrangement = Arrangement.Center,
            modifier = Modifier
        ),
    )
}
