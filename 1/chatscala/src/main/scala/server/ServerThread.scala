package server

import java.net.DatagramPacket
import scala.io.StdIn.readLine

//class provides server commands support
class ServerThread() extends Runnable {
  override def run(): Unit = {
    while (true) {
      //read command
      val command = readLine
      command match {
        //print list of all clients
        case "clients" => println(Server.usersThreads.map(_.nick).mkString(", "))
        //send ascii art to all clients
        case "ascii" =>
          println("Sending ascii art to all clients")
          for {client <- Server.usersThreads} {
            //create packet and send
            val sendBuffer = returnAsciiArt
            val sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.inetAddress, client.udpPort)
            Server.datagramSocket.send(sendPacket)
          }
      }
    }
  }

  def returnAsciiArt: Array[Byte] = {
    """
      |        ..uu.
      |       ?$""`?i           z'
      |       `M  .@"          x"
      |       'Z :#"  .   .    f 8M
      |       '&H?`  :$f U8   {  MP   x#'
      |       d#`    XM  $5.  $  M' xM"
      |     .!">     @  'f`$L:M  R.@!`
      |    +`  >     R  X  "NXF  R"*L
      |        k    'f  M   "$$ :E  5.
      |        %    `~  "    `  'K  'M
      |            .uH          'E   `h
      |         .x*`             X     `
      |      .uf`                *
      |    .@8     .
      |   'E9F  uf"          ,     ,
      |     9h+"   $M    eH. 8b. .8    .....
      |    .8`     $'   M 'E  `R;'   d?\"\"\"`"#
      |   ` E      @    b  d   9R    ?*     @
      |     >      K.zM `%M'   9'    Xf   .f
      |    ;       R'          9     M  .=`
      |    t                   M     Mx~
      |    @                  lR    z"
      |    @                  `   ;"
      |                          `
      |                 .u!"`
      |             .x*"`
      |         ..+"NP
      |      .z""   ?
      |    M#`      9     ,     ,
      |             9 M  d! ,8P'
      |             R X.:x' R'  ,
      |             F F' M  R.d'
      |             d P  @  E`  ,
      |            P  '  P  N.d'
      |           ''        '
      |ss
      | X               x             .
      | 9     .f       !         .    $b
      | 4;    $k      /         dH    $f
      | 'X   ;$$     z  .       MR   :$
      |  R   M$$,   :  d9b      M'   tM
      |  M:  #'$L  ;' M `8      X    MR
      |  `$;t' $F  # X ,oR      t    Q;
      |   $$@  R$ H :RP' $b     X    @'
      |   9$E  @Bd' $'   ?X     ;    W
      |   `M'  `$M d$    `E    ;.o* :R   ..
      |    `    '  "'     '    @'   '$o*"'
      |""".stripMargin.getBytes
  }
}
