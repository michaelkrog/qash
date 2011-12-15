package dk.apaq.shopsystem.file;

import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.wicket.util.crypt.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class DefaultFileSystemPopulator implements FileSystemPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultFileSystemPopulator.class);
    private String themeInfo = "{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}}";
    private String templateCode = "function renderHead(response){\n"
            + "response.renderCSSReference(\"style.css\");"
            + "}\n";
    private String templateMarkup = "<html>\n"+
                                    "   <head>\n"+
                                    "       <link rel=\"stylesheet\" media=\"all\" href=\"style.css\" />\n"+
                                    "       <link rel=\"stylesheet\" media=\"all and (max-device-width:400)\" href=\"style_small.css\" />\n"+
                                    "   </head>\n"+
                                    "   <body>\n"+
                                    "       <wicket:container wicket:id=\"placeholder_1\">\n"+
                                    "           <div wicket:id=\"placeholder\">\n"+
                                    "               <cms:module name=\"label\">\n"+
                                    "                   <cms:parameter name=\"text\" value=\"Test\"/>"+
                                    "               </cms:module>"+
                                    "               <cms:module name=\"image\">\n"+
                                    "                   <cms:parameter name=\"path\" value=\"image.png\" type=\"Path\"/>"+
                                    "               </cms:module>"+
                                    "           </div>"+
                                    "       </wicket:container>\n"+
                                    "   </body>\n"+
                                    "</html>\n";
    private String stylesheet = "body { background:yellow;}";
    private String imageData = "iVBORw0KGgoAAAANSUhEUgAAAF0AAABwCAYAAAB8Q3wrAAAABmJLR0QA/wD/AP+gvaeTAAAACXBI"
            + "WXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH1QgeFDky0+2bxwAAIABJREFUeNrtfXmYHFW59+891d3T"
            + "07OQZSb7JDMJ2YOGgKzKGuLCE1AQ7/X66adXFPQDRRFwufcJ8aoPuOCGHwgqopd79YPrihAWCWFJ"
            + "JEgEwgyQTFbCOJOZTMJMd093V53zfn9U1alT1dU9PZOAeLmdpzLdXd1dVb/znt/7nncrwmvwUEoR"
            + "/k4eQgh+tY9BrwWwzPz3gjmI6FUfEDpSQI8D2NeD9PN4B+RwBoDGC3YNINPfm5THgMujfW484NOR"
            + "ApuZqQKt/N2Ab4IdRzNExEcCfBor2Ndeey35J7R48eIQoJMnT6boyTIzmBlNTU0VqYiZkclkXnW6"
            + "yefzZWCar4eHh5mIEHcNBw4cYPPzzz//PPv7/PeuvfZargV8qgXstWvXatC7urpoyZIlGuxp06aF"
            + "gJ8wYYL+zWpAptPpv7lkFwqFqgPkPz906FAI8N7eXj14XV1d+nNLly7Vn1u7dq0aM+hKKbr22msJ"
            + "ADo7O8kDVBw8eJCy2Sw1NDQI/7NHHXWU/h1fohOJBACgubk5REnpdDqWol4L+qlAGSgUCmy+Hhoa"
            + "YgBwHEfPAH+fPwAAMDAwwESE+vp67u/vV01NTdza2spLlizhasBTLYDv2rVLNDY2WgcPHrTS6bSl"
            + "lCLbtkUmkyEpJWUyGRJCaECJSD9Pp9MkhNCg+hfe0NDwN5f0XC4XGnAiglIKhUKB/cHwr6dQKLBS"
            + "SlNUPp/XVJLP52UqlVJDQ0OyublZtra2qtbWVl66dCnHAU+VAO/q6qL+/n7at2+flclkEkNDQ8lU"
            + "KpUslUrJVCqVYGaRSqUIAKVSKUomkwQA3nvwJEAAgGVZZEpSMpmk14NiJSLYts3muUgpGQBGRkaU"
            + "/36pVGIAsG2bS6USMzOKxSKIiEulkiIiWSqV7GQyaRcKhVImk3EmT54s586dq+KAT0RPZO3atRrw"
            + "3bt3J5LJZGpgYKAulUxOUSX7/SlhrSKpFgmiJlWyQUQoORIlGoEggRFvKAmEVypM60oLkGoLk8M2"
            + "yCtbXRVfM1gbjYoVwIFhkCQBZh5OJ5JPSSUfsonuKhQKI5ZljQwPD49IKW0ADgDlCTfHSvqaNWsE"
            + "M2PDhg1i9+7dCcuy6vL5fLq+ru5si8TXhRAzfe0et/nAVXr+WgM93gHwga30PG5TSv3Vls5ncyMj"
            + "T1mWlbVtO9/c3FycM2eOc8YZZ6i1a9eyD3yZpHtSLurq6pJDQ0Pp+rq6lQlh/TsRYdasWbjsU5/C"
            + "CSeeiJmzZh7ZZWENdFPpcxXfD0lt+DPuUwb7zw1gfaE0PxPsD2ZAT08PHl6/Hr+44w709PRMTyJx"
            + "a0N9/aXZfP7JVCrF2WxW7du3jzs7O9mUdMvk8ocffpi6urpEb29vwnGcOkuIGUkr8SshRN0FF16I"
            + "G2++CcuPXY7m5ubXpwNoHMegKstOdxYGSpYjv9XY1IhlxxyDc1efhz9t2ojBwcGUIDqFBN1fsu2R"
            + "UqnkCCGkbdt86qmnoqurKww6ANHV1UV9fX3CsqykUqo+QeIzlmW9ddasWbjx5puOONh/O6hrc8D4"
            + "E4OIItZX+BupVArnrHo7Hrz/fmSz2UYhRE4xbxVCOLZtO0IIOW3aNOXb9CLquCoUClQsFoXjOAki"
            + "WklEuOxTl7+qgPNr8Mt8eEogvMI2dBF5W1NTEy7++CW+HjtNCJFRStU5jpPM5/NWf38/rVmzhjTo"
            + "5qOlpUU0NDRYzGwJIRYCwNnnnIP/rg/2gSN3E4IghPD8KVRV0bv7gv0rjjvOpQyiY4QQ9alUKp1O"
            + "p5OO44jh4WHyFpmU8KU8qtFTqZTwf7ialD+yeQduu/NJ7Hz5ACwSsCzhnjgRyBtS4T0xz5/8k6XI"
            + "1b/a6Bpiz+AyYVbsmtSsAMUMpRhSKkhWWHb0VHz8H0/A3NmTXLwoPIemzZiuqSiVStVJKRPMbCml"
            + "RLFYpFg7nZnR0tJCg4OD7uCPYs49snkHvnTDOlhCIGFZSFoCwvIkxfU5Q4iwaUjjCB686qZiaL+A"
            + "UoBSCuxSLyxBsCWhs7sf13zjPlx/1SrMbZsc6A1iVyGz6XWkBAArnU5buVyOfNDXrFlDIdCXLFlC"
            + "nZ2dyGQylM/nfZdlxYv4/u2PwRKERIJgWRYSHuCBpHt/yZ/CVJuuO1JSz1V+P2ZAfMtRCQYrciWd"
            + "CIoUmAhEEoWijV/c/Ry+9MkzjJkSKFchBJRSEEKIZDJpSSmpubmZKq5ITWO/rq4ObDtVr6n3QBYJ"
            + "S0AQIWEBluVyoU8xlhAQ5J6OEBSiGOZg5frqqlYqMw99Ozt0LgCUciVWMSCVAikGoEAkoFiClcvx"
            + "j23ZjWQygaJtVzyqZVlEROTFGaiqG2AsD0GuJLtgu4AnEhYSgkAWwYIr7aRdA+XK5/WgSvWK07PF"
            + "hWKQEGDpOrscR0IIAcsCpGRv9lL54Br0VY2bE4fjWiUBCOFCaBHBEgRB8LR/YA34Ei28i3q9QO7O"
            + "NoKAqzQBBjGgwLCYoCyCYAlLEJQiMBEsiyA5bkJy2FCooqMSoymaqiaT908IV6JBAYcLIndAjNfj"
            + "UZiTJ2TQMjFwA7+4q//IEw8zLCIo9qlQuFTDrlQr7/pIuUBTOd5jMgg06IsWLSJ/mVq75LvEqDky"
            + "ckEEYVhp7El65d+dPX0ijl0yEwvmtmL29AmoTyerHv2lvx5CvmDjwMEcDhzK48DBHAYO5vDCzv1j"
            + "dPH6vhnTnGVEDVxlhObM6xrNUmJmzJgxo9xkNEeodqrxzCUW8NUFm2dErOfD+WcvxcJ5U/Dizn4Q"
            + "AS96wLy405XchXNbccqKdrRNn4C26RNiFoTl5zRr2lHuk/aWsn0HDubxUu8hbNvVj227+rG351Cs"
            + "ZaM4+H2COysVlF4wmba4AMEhBQIheko8BtwSYw1vlUkIsyfpHHjllAIsC/764ZTj23H+Ocs8cKcA"
            + "zFh91pLQb/m08XRXDzZu2Y36dBKZdBLMQNv0CVg4t3VM9vikCfWYNKEeb140HQAwUrDx9PM9ePr5"
            + "HvzluZ6Ahz3B8U1GKHcgfGsG/qD4ykjFKSWOKtHxgz7aw104WO5Jagc/gUGQzLAU0DZrIi5+34mj"
            + "unAXdrSG/obCaiOl8bhJQsdM1yVw0vLZOHH5bLzc+wq+eesG5Is22FuBQrtxXSvEo28oDgZEeaOh"
            + "lBr1uFWtPjNloFoUJRZ0NqRC2/iAkp5jH8D7Vx87Jp95FDBmRmYUbh+TN41dWnrvu45xV52K3Y0Z"
            + "UjGkUlAesEq51+Nel7dCBRuX4c9wrjkatXbtWhZROonmfYwGPHv+CddP4S4qGO57Cztasejoqe5v"
            + "jCcmOsavxHM/xy73T1nRjklHZYLz9zYGIKUHuGI4SnmLpfBn/EieOQAMronXNegvvPCCTp7J5XJc"
            + "KpVGB1yp4KSl0pIj2R2E45bNGh/YVZSnOQNq24yQmxEVAgNnnXw0pMfh0p+lSnl/GRLuVFbSvS7l"
            + "L6JUEGXSlk+ZICq2bVunbvT09HAZ6P5OP5PJVAjaLxFDL/qiEHjl/Ck7Z+bEw2AEHlvILmaQKg2c"
            + "/86KJTOg2PUganqR7AHvAW5KuPE8HNKrPDP9TAIAuOuuu4IgRjQVLAS2/6sc8FdoU4Cj2PPMeTQj"
            + "3RPb/Mze8QFebXbw4Q+cjh1MakDbtKOgpAs2szdTle/S9a7H00+OD74fRYLBM2Mwt8VoRv1oFCBZ"
            + "AmAoViF+l1Lhiaf3IpcvHTHAKwEZK9Fc7RjBB5YvnhkA61Ejez50ZfC9lMF1BjM8zOe14hYC3U+K"
            + "JCI9Lar9gJQKrFyHvz89TcnoHRjGT+7afOQAr1E/jCblbIC2aF6rZ6koF2jJkOwC7lszUvO8H9xQ"
            + "5WPLYcD9pCUgSMuryOl+Ll8QGanqn/OVBhzHpxtPqUoFqRh/3LgdD23qPjxKqSa5Y5Ry8/MMYPG8"
            + "KQZvwwVcweN26bKH8iwa6Q5AYKZHqMWXd4LnnQxc4zt27ChXpF/+8pcZAHp6eoJ0YkZVXwkbphQ8"
            + "zS/Zs3XZt2gY3/3pI/jR/3uiMmg10FittFLN6qlwcLRNn6BDcu45S48i2eNxFYTvyuzyCLWA9UIR"
            + "AIrFIo/K6WVal6utSNnjdYZCYGr5CyTFrE2w3z3YiS9+8x70DQzHSt14TMexWj2hpCJToU7IaOtE"
            + "KgUpEXC6/xcc0IwfRy1bAATmpPKXr0Ao07cMdN+k8TmopoWRphh2bV6lPG537XipAEcpnPDm2eho"
            + "m4QvfuMe/MfvtiCbKx4W4GOhldj9hs0+e+YESGY40qcWCSldZeooBUcqOE5g2bD2e8TLu4+b4zgc"
            + "h2GZ72XHjh08ceJEd1o0NsX6GUIKSSk3mEsM8kJbUArTWppw6nEdWL5kBk4+do6+5ovfdxIefHwb"
            + "vvqDB3HMwmk4b+VSNGbqDhvwyhRULtnR50oxpKM8HYXAYjHcA9ot4HE6x1CLXjC5irQivRyWw8t3"
            + "cCkGmuqTeMub2nDaCfOwfMlMTGtpqkgjK0+dj5WnzsfOlw7gjt9uQUOmDu8+xwU/atLVBHgEyp0v"
            + "HcDOvYOYMrkRHW2T0JBJVZRynxKkVHqlqwx7XCrXbFGGdcYRSqk0yxzH4Zq9jIcOHWIzaZ+NGJtp"
            + "0CzoaMXZp8zHW45pw4plsyL+jdHn+9y2ybjk/Scjly9h4192Y+rkJhyzcPrYAY9M4Y5ZkzG3rQW7"
            + "9g3iP3//DHr7h5DJJDF31mTMbZuEYxZOC/1e78AwfAtPKte/KPWCTxm+JZc2lVShAIYv7/r1KJZf"
            + "RUkvFoscFDGxKRg6gPHL731oHIGP8kdDJoWVpyyoSsyVAY93sTIrtM+cgIvf9xYQEfoHc9i4ZQ9+"
            + "+Is/YceeA8jUJzG3bTKYGc+82Ov5Zvz1htLuDX9Vyhx4HVUUDFORBvTC4/KnVwfyyKRJjEYnowPO"
            + "Ff2JbCxYWiZmsPqsRTjv7MXYfyCLH9yxEY8+uUsHWnzPqKvCOACYAz73Jd6kp5CU8zhWpGMH/vDA"
            + "Dky4IwQ4VzYcg8R+hZZJGVzyDydqIF3rJFCYjmeBOVJ6lkwAesRoKZP2cfleXu3HWMA+LMD1fyFj"
            + "Tr89rbUR7TMnwpHu8l9KBceR7mtHQUppeBehPYs+t8dJOdc4+8VrAjICF/BoYIel+3ABNxUthywX"
            + "Zsbb3zZfL+0dH2RDqrXzzuN0P07AVTi9FtwTNfMuyq2XqizPY+d+rvKdOCulEuBly3Tmst9nAMsX"
            + "z3CtFUbIRmcOWyI6GGL4WxgYl5TXbqdznLKiI6ZYeZQBqoVmOMaPwxyvZP2vzW+fjEw6geFcKYjv"
            + "mjFQDp5HAY+1XGq8XnEEiGPcgId5m0f1Co4PcC6nJaOA69ilM7RvRSEasDGk2ZRwjji3qq2QXg+K"
            + "tBawY7m4bDE5PsA5Av789hY3YYoAQQwCu+mAAsZ77mcVVwjPlQWpjwS9HCGga5GGSnY7x+iJ8QEe"
            + "Pp8VS2eCTErxE6giiow8CdVGQVTKufYZnxgLYLXmfpaPeK01otWUaLkNPibAQ4o1eD5/TgtYhT/l"
            + "d3Vh0/XhAc9G/bMJ/FgkXYyFv4OUhkobG8WvtfF9Nbudq9AJq3jAN2zeUdWMjILf1FDnpnx7mcUa"
            + "YAoq50KbRz1RWuEx6LZxcDpX2ca2QKpmt4fB5lEGIZDwLZ092L57oDLgEfAZjAXtUzSaJKxQdZ1b"
            + "YRdU35WXNBjeyhoDLq+JIg0vkKrzH8csjrjCjAgrW/b89W/BjT/fhGy+VAFww5733m9qrDMAJq+q"
            + "xIIgC0RCD4Kfa29EMw1+rz2LTYxVtsc0B0yQmUc1DeNArUonZQMANGZSOHXFHFz+5d9hOFuMATyi"
            + "AJVbcukW6wgIEl5Bg9lsQsASli570SXrrDNn9QDWUhQgataMZclGMVwe3UYZKq5GI2Vgx9NJnBl5"
            + "0TuPQVMmhSu+8nsM54plgOvjK+89r2rEl3bhFa8RkTcIwstdF17xGsFs5hA9/mjAH6HFEcbB56OA"
            + "HUNDHBlMjjFF/ddfuPQM9A1kccVX7kY2Vwy7XhE2CQUBlnBrpizL/+vzukcngkDCnRGChDFbfEl3"
            + "nwrL+ttyehTEcqCrSHZkCV8m3WWexLAumNrShM9fejp2vXwQn73uD14g3ACczdJGAWFZEMKlGN88"
            + "TljkFSaLkGUDITS96GIC7xwEuTPF7OY0Pk6PMw8rgBrPLlwRaFWBRkJgx0l3Nc+i9xOnrpiDqy8+"
            + "Dbv2DuJz192D7r0HygAHGLteGvSqAuFKumXBsiwISwQFbN4sEBZpQ908DzYcbQTSbbXGBbplWfHm"
            + "YSy6lU1J85XOBVTGi1HAjjMjowNQli/OwKq3zsfnPnYadve8gqu/sQ6btuwNAZ7Nl1AoOW5JprcJ"
            + "fwXqVYxbllc1COH2PzCWphxxdvkV5kIIamxsFOOTdDoM6yUKchl9cAwVxYBdwYyMDkAcZzMYK089"
            + "Gp/98KkojNj46k3rcesvn9SDsvOlQbf+NSFACQF4AFsgozbWK8v0FkZ+gwlNMRGPpGVZEEJQIpGg"
            + "MYPuj5op1H7Ji4KxcQRYA+AQyBVWnfc/ts3l3Epgxzq4UEHaYxSmAlaeMg/XX/0ONDfW4e71L+CK"
            + "r/4Bu/YNonN7X1D57f0VloBICpde2KWLRMJyzUgGgnYJbBQohE3aZDJJXguuxJhAdxsqiHjCVlwF"
            + "7comoynRzIwHHtuGb9/2GD5yzZ341o8fQd/AcKzS5Qr0ggidRM1DMpwpS4+egh997UK8efEM7Ol5"
            + "BVdetw53P7wN5PE1EUI0QwR3AKzAXhcW6XpYk8d9W900F+vq6sSYHF7pdJroMAvKuaoXjLH1xV58"
            + "9/bHIQRhpGhj/RM7sf6JnVg2fyrOP3sJTjq2Lda7GAqj6MJb8kpRwt4qF/yg1DaTTuDfPn02/tzZ"
            + "gx/f+RT6BoYhLAFWrCXYH3QBgInBLMBQbt0pEZQMB6j9sk5m1l0wfKFKp9OYPXu22LNnz+ig19XV"
            + "kVmRET8VqJJbcVQvY99AFl+9ab1X2h7+aFf3fnR178eUyQ1YfdZinHXyPDRmUl65ISKhuNCrCIOx"
            + "lnSOROyPWzIdvWcswG3/tQXEbgMGFZJWPyHWHUypXAuGlUIiITyvo5GS4XN7pFAulUpRPp+vjV6a"
            + "mprEqCoyIO2aHGH+q2y+hK/dvB4jRRsJT1HpzV8VEmFgMI/b7noKl/zLr/G92zdi/0DWhDekLMvo"
            + "hMxFFZWlSDAY92540VOOBHjmou5P466ItDvA8kwaS5BhwFHYVvebOkR65EydOlXUSi9QJXv8gekK"
            + "Djci4Pu3P449+w7BMtrumV+JdicZKdp4ePNOPLx5J5bOn4Jzz1iEE97UVlG6Q2qO4593bd+PgcE8"
            + "LBDMJDmLSBfuWiKokwW7K1cFQJdoRRuqeULvWjpCt+mqr6+nefPmUXd3d2XQlyxZYoWydXk84ecI"
            + "/3pT7hd3P4Mnnn0JwhLGILLRDAFVIyZd3f3o6u5H66QGnHvGQpx+Qgca6lMGbVOsko0OyrpHXQXq"
            + "T3e3wY4rzYLdymn2y9jBYN+ty25fmJD1YjTMrOR3Wb58uagKei2cPKaHJ9Gbn9mLX97zrO4HoyNS"
            + "EHElZ1Vn1oGDOfzs11tw17qtOP2EDrzztIVondRofI+NvjfkoeWyfP9gDk891xuafWbna9ahIVff"
            + "SGYI4SlQAUBGCJMI5FVamyZjXFbCmMN1Y+UXQyCwa98gvvezTa7TyCuFdNcYkebHjDHFBvMjDu7d"
            + "sB33btiO45fNxDtPX4AlR7dWcOe6r/7rvk6jlZV3Lr5PRXkFucoD2nvt855gQHqKmUloveabwWbj"
            + "zEoGSCzoDQ0NNDw8jMPkFw1oLl/EdT/cgJGCHVoDaAeS2e4opnNgrY8/d76MP3e+jJaJGVz49qU4"
            + "ftlMt5OGIXH9gzk89tQe49jk2Ybs0UMYPCKAmNz0DPJNUw55PcOhSv081HPBvINC2aSeN28eAe6t"
            + "C4KDj26fRB1gAGnFct0tG9A/mA27FtiQoGg2hrG+io1TjrIdOJjHLb94Eld85Q/4+W+eRv9gzgUU"
            + "wK/v7wodxwc5OoOJKOjrgqCLkx+gDsxEqkgCvhvAr8a46KKLaGwpGGOmdVd8v/+zx7F1W592IrHh"
            + "w1bGtRLFU9jhaJNcwcZ9j23HfY9tx4qlMzBnxgRXyslsfgndUc9MxXcHwvN3eLY7m/1fYJboc8j3"
            + "YipT27Y5lUqFqCYW9FwuF12C1EwmfsiKGXhoUzce+tMO3aEuZExQ8NvV2mQeqSZrWzp7sKWzJ+aq"
            + "qMxTGM6A8AfHnB6GK8NsbI/KcdJDhw6x78tKjK5Ea0/SMy3knS8dxPd+tlH3Y/SlQgiCWeHh/zyN"
            + "TX+O3zXB0Vwes2SFYFQiuoufmCwC5sDXAiMHUievehchpWT/hiw1Wy++ncpjnOR9/Vn8yw33uQdW"
            + "yo0xEns3+vC53C0SE1pxcm0ehcN8RMdUKRd4RrilSNiZFdyeQSnGW4/r8LJ9OagrZRhdkgJ7vVAo"
            + "lN10JRb0np4eNWHChFAAd9SzZ5dWsvkivnrTegzlSi6P+/avZw0IChSsEIHkvFa9Gs3sMD+dwlwH"
            + "+jQS9HZRelUqmdGYTuKfLzoBuVxel8cglGLtXotZog4ABw4c4NbW1uqSXiqVOFFXF/BWFcVqevt+"
            + "+J9PYPueA0hYAordRQMRQUjXTFS+1QJypQyBQnstHwQj7Mhh5xmrwGvo73OkwvJFM/HpD78NExoF"
            + "hoYdA3Dlti0xZodSio07yfDBgwdHB30slovJeVd+9G34wifODnGzL1UcueDoQoqrOHCq+XW4QnI4"
            + "l5cEhu9vEbmAUCQoJlGJwcgOZzE05Bgc7rdcQaidVKUa0ljQzYrpTKrO9SfXwOm+9EqlUCgWEG5d"
            + "XOnOEzzKICJcLRQtqoqWWhqR+WpAmtkFbLhlORp60+8rXXPEylSa0qUmZje7Xfl6QkF4wEspWSnF"
            + "iUQCd955J1eV9GKxyGgCatVu4Ty+QOuHAgtVZ038rRSiaexlCUMRsENUEUkmV9EoE8zkf6PExSu/"
            + "9wHX31dmlwtvILyaJCgK2lBFnHxDQ0M8adKkGq0X9qwX5jH5Xtjg7XCaNcVOe479kRiqiO4y/Clx"
            + "YJeVrISyCljXjiIi5aaP3rRkOjufQyaTQUtLK1gxMpl6D3CvnJ3ckkgyFkZmD6+aYqSxDpsaknaD"
            + "xVDwT/l9U1iFCqe0a9ToCeZ2SkKonRMrPyQbNGZzW1gpr2ue8laMZplhUIDr827QVEHpdn9aYSqj"
            + "IYN/HI7QjlLo3LoV99z9O90RQ7FxDd4g2ratiKj2hgyHDh3ixsbGoAMd86gmXTabxY7ubrx5+XLd"
            + "CHjH9u3I5XOYOnUqpk6bHjLFtj7zDAhAx9y5aGxsjOVz/+n+vj709fUBYHR0zEWmsQHhZaT7hVw2"
            + "i527dmHZsmV6QPf39aG/bz8yDQ1ob28v4/No+kTnc1u9+6M2YE57u2eDExhuA55sNov6+nrU1dcH"
            + "xWFe9ExJDq2ybaOpvdnZqKYVaVSVxgWsr7j8cnR3d2P16tV4z3vfi3/94hexv6/PTeMgwsmnnIwr"
            + "r7oazz77LG74xteRzwVxw3/64Afxgf/1wbIptWnjJtz6w5uxv68vtOfEk0/G+z/wAXR0zA3RyBev"
            + "+Tx27dqJ8979bpy7+jx87zvfRufWrfp77R0duOzTn8acjrmuO9a7qFw2i9t+/CM8vP4hLakgQmtL"
            + "C952+pm48H3/AGaF2e1ztD3eNnuOO3s8zlfSyNxldzVayYIp675NRAkiqk8kEke1zZi5BwA2bX6y"
            + "+vKOgTPf9lYAwMxZszA8PIxcNlt237r5CxZg+7ZtXtaYr2Xd8/ra9ddj+YoVWnh/++tf4Zabbqoo"
            + "DA0NDfja9V9Hx7y5+jvnv+udAIAFCxegp6cH2eFs2femTpuGb37nu6ivrwczkM9n8bkrrkD//v26"
            + "R0uUXk8740xcetnloaYNGnB/YeTRyz9e8G4QAQ74vKGhoV7btvdns9lDzDzCzA4ArjGXkRH7Twdl"
            + "WTfjeXnfPkyfPh1/WLcOGzdvxoc+/GGvGllh24svYv6CBfjN3b/HH+67DyeedBIcx4HjOHjskUeQ"
            + "sBJQrPDM03/BTTfe6NXlS3zmyivxyMaN+N0992DlOasgpcTQ0BCu+dyVGMmPePmQrD//fNfzkFLh"
            + "//7wZjz0yCP4+g03oC6dhpQSPS+/jB3bt2lu/9qX/w1/7emB7Tjuuf3+93jg4Q24/pvf0t9Z/8cH"
            + "8Zu77tRBaOnpkCjgfo+Yw2rIwCpcO1k1dRHQrfY+edn/QUNjE4azOZz/ngvcLs3Krbe/9BOfQKah"
            + "EQzC+e95j27B99K+fUil3EbG9917Lxwp4UiJj116Cc5dvRqHXhkCiPCpz3wGS5YtgyMlXhkawpOb"
            + "n9Cc7H/HkRJXXnUVZs1ux+DBQ5g772gcf8KJet/uXbvAzOjt68Wzzz4DKSUaMhn84OabUZKMgYFB"
            + "tM3pwFWf/4IGcf1Df0QqlXJvUIKgYWZgJMQ3Ozbv6lsT6EYUJHwbyJhNKgmpJJgZxx//FhS9Xr0N"
            + "jY16MJgZp515BorFkmfnqvKCA6Xw+KOP6vfede65yOfzHge7BbYrV63S+zc+/ljgIDJ+66RTTkap"
            + "VNKKc+qUKXqfGw9VeGLTJn3cM88+C6WSDenYevYuXroMkye3QCmFl/ftQ+9fe3S3I7MtCXM84JU4"
            + "/fDoxfgXOIwU0pl6Y5ACwJVSkI4MTEbdG0uF+vUODw/r9ydNmgxptCJkxWhtnaL3D70yhGTC0q/9"
            + "ra4ubZiTXudQ81gMfRzv9gkYGs5q01Yp10ZtaW31msFJlEpFw4wMzF8Ywuc/xt1kp5JPnSpQEVEQ"
            + "HjJz/HyFo60eY9ntKy+38WTQ2blsxWmYiL6drSnNkDrznJVCSOf4+5n9ppZKHz+uE7XyzMFo38VK"
            + "XauZuSzrbBySHpOLXv3us4HJFcp8imSJsFlCNE7xAAAGPUlEQVQcxUbfFPcCOubN9XraKjz8kGfG"
            + "6UWVwuOPP6r3L1y8CEpJbS/7G4xFEThYTCkjct/e0aGP/sADD8BKJEIgZnPD6HruOd1cbf6CBWUK"
            + "05R4xXzkmuyMhV6Yw8DCGAzdKBJBBUZcrcGqt79Dv/7RLbdgODukL6i7uxv337tO7z/vvPM9HRGO"
            + "njOMgVIo2w9mnHDiSchk3IXWC13PY/2DDwSrUTBuu/UWPUPOXrnSawUbDKJ/hwHlz06uLdxTE70o"
            + "Q1prCTb4ZS2msyzUH9GcwgZNuIOlcM6qt2PdPfdgR3c3Xnj+eVzy0Y9i1TvegWw2i/vXrUMu69rf"
            + "F1x4IWa2tWHw4Ct6cM2T8Kvngt7uKqAebxA+cvHH8P1v3wAA+NbXr8dTf/4zpkydiue2bkXXc+7C"
            + "qqmpCZ+8/DIcPPSK5m8zU5cVl/cTOFzQ4/u9hB+Zhgbkczk0NTUZ9OItZBobkctm3X26NSqjIZPR"
            + "329ubtZTtqGxEd/8zndw5RVXYGd3N/p6e/Hzn/40dLwLLrwQX1qzBvv3D2hnWENDA3LeObDfSsST"
            + "ejNk1tzcpCX/zLNWghXjxu9+GwDw8EN/DB2nqakJt9/x7yCrDk4xH8vjJr8bM77iEFgxK1JBREkh"
            + "RLq5sekzAPC/P/LP1avSGZg9ezayw8O49JOfwJw57SgUCvqDEydNRKlYxNWfvwZTp013c2oATPTc"
            + "nVNaW3H5FZ9GIlkHx3HAAJKpFM47/3zMmT0HLS0taGpuxsxZs7By1SpcedXncN57LsDAwGBIcc2e"
            + "046RkTyuuuZqHDVhIkq2rQd/5qw2FPJ5zGmfg49+7OPI5wvapds+twPvfNe5mDZtKizLwsxZs7Bo"
            + "yWK8/wP/hH9dcy0KJYl8bsSQbugUac3xntT/9ld3QRDBUeo/isVi1nGcnFKqgOB29hXdAGnLso5q"
            + "mz5jLwA89MijFfLlwrNBCIFkKolioVg2IxIJt/5S39nQEAQrkXBDYraM+LsDPVFfn9Z0VCyWIkVW"
            + "wfNkMgFmhm3b5S1CGMhk6jEyUoCU0sj4DXzqqWQSyWQSDEY+P+IKAaMinZiSnstlcdklH4UlBEbs"
            + "0ruGh4d7C4VCv5RyiJkLvhsgMQo1DwNo2r59O+bNn18hudGMrCs4hWLs52zbqdjjyynZMYELDvnR"
            + "8/mRkKJiDvvl/c/5N1DhyPs63JbNhQrKOOSTZxSKRYwUCoGPBVyBUsp9MC8Et6HbI6V0lFKSmRVz"
            + "OJlbxBsrzMyslFIPAcDtt/3EGN0aNmX6oiMdLxRHfgvad+JfABvmIZTRDJ8RNs08/eD70s0uoaFz"
            + "8RdHZStr3+42/OuqwqrbKGlRhvmp/MZrzHjgvnv8Zcpztm2XpJQ2M0tXjwcSWMbpHsVYRJS0LGt3"
            + "XSr1oZf27sXUqVMxb97RQXZwjKk3Wt1j9P5uQQVFuC9T+CWDy+KmbLT6K6cQVPKZmwELRBUgl/Ws"
            + "qSbd4QUR8MB992DD+j9CEKEknetGCoUe27aHlVIjAEpegjUDYCs2xkwkAIiSbRcaMpmMEOK4jY8/"
            + "hmw2i7bZs9HQ0FhTpxc2w16IaYAV+xYjWmzNoWAFl6dMRChExzwRDTDHgx1LJeBI8lEQsDBdvAP9"
            + "/fjJrTfjwfvXuTk9hO9nc7mNxWJxSEo57ClR27SiY24fCwKQIKK0EKIxkUhMnNY6ZY0lxEWak4SF"
            + "/3l4Ti3peAmxBCb8ZDiXu2NkZORgqVQ6KKUcZuY8M/ugu2ntFUAXRJQkonoiakokEkdNmjDx9HRd"
            + "3cUEvOmNAGYt6xyjVGezI+Wvs/ncpmKxOGTbti/leWYueeainrxU4bd8afeBb0gkEg2JRKKxPp1u"
            + "SdelFwtBFpEQZpT/jfPQ/K6Kdukvtm2XbNsecRwn7zhOVimVV0qNGIArcyypygAKABYRJQHUCSHS"
            + "3lZnWVZSCGF5CykyQ3L/7eE2CiyUUoqZpZTSVkoVlVIFpVSRmYsepUiTVkbLYjGBF4bU+38tT9kS"
            + "vVHQrmJaM7NkZttb/Phmoqxka1ANlOX3gRREZHnPfQvnjcgtkTUcG/eLZWk6USuph9EAo5i/5laW"
            + "QvYGopdK6VcAqjffrRUpGsd33pCSX4vx8z8G92trZQIA/j+xsJhK0jUO2gAAAABJRU5ErkJggg==";

    @Override
    public void populate(FileSystem fileSystem) {
        try {
            Directory root = fileSystem.getRoot();
            root.getDirectory("Organisations", true);

            root.getDirectory("Users", true);

            root.getDirectory("System", true).
                    getDirectory("Themes", true).
                    getDirectory("Standard", true);

            root.getDirectory("System", true).
                    getDirectory("Themes", true).
                    getDirectory("Optional", true);

            Directory themeDir = root.getDirectory("System", true).
                    getDirectory("Themes", true).
                    getDirectory("Standard", true).
                    getDirectory("Basic.theme", true);

            
            Directory contentDir = root.getDirectory("System", true).
                    getDirectory("Content", true);


            File infofile = themeDir.getFile("bundle.info", true);
            OutputStreamWriter infoWriter = new OutputStreamWriter(infofile.getOutputStream());
            infoWriter.write(themeInfo);
            infoWriter.close();

            File templateMarkupFile = themeDir.getFile("Simple.html", true);
            OutputStreamWriter templateMarkupWriter = new OutputStreamWriter(templateMarkupFile.getOutputStream());
            templateMarkupWriter.write(templateMarkup);
            templateMarkupWriter.close();

            File templateCodeFile = themeDir.getFile("Simple.code", true);
            OutputStreamWriter templateCodeWriter = new OutputStreamWriter(templateCodeFile.getOutputStream());
            templateCodeWriter.write(templateCode);
            templateCodeWriter.close();
            
            File templateImageFile = themeDir.getFile("image.png", true);
            OutputStream templateImageOs = templateImageFile.getOutputStream();
            templateImageOs.write(Base64.decodeBase64(imageData));
            templateImageOs.close();

            File stylefile = themeDir.getFile("style.css", true);
            OutputStreamWriter styleWriter = new OutputStreamWriter(stylefile.getOutputStream());
            styleWriter.write(stylesheet);
            styleWriter.close();

            File imageFile = contentDir.getFile("monologo.png", true);
            OutputStream imageOs = imageFile.getOutputStream();
            imageOs.write(Base64.decodeBase64(imageData));
            imageOs.close();


        } catch (IOException ex) {
            LOG.warn("Unable to populate filesystem.", ex);
        }

    }
}
