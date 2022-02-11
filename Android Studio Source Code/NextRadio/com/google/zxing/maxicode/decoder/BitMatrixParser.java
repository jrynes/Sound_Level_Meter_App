package com.google.zxing.maxicode.decoder;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.InputDeviceCompat;
import com.admarvel.android.ads.Constants;
import com.google.zxing.common.BitMatrix;
import com.mixpanel.android.java_websocket.WebSocket;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl.Basic.Nack;
import com.rabbitmq.client.impl.AMQImpl.Basic.Recover;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

final class BitMatrixParser {
    private static final int[][] BITNR;
    private final BitMatrix bitMatrix;

    static {
        BITNR = new int[][]{new int[]{Service.ERPC, Nack.INDEX, Service.LOCUS_CON, TransportMediator.KEYCODE_MEDIA_PLAY, Service.STATSRV, Service.CISCO_SYS, Service.NETBIOS_SSN, Service.NETBIOS_DGM, 145, 144, 151, 150, 157, 156, 163, 162, 169, 168, 175, 174, 181, 180, 187, 186, 193, 192, 199, 198, -2, -2}, new int[]{Service.NTP, 122, Service.PWDGEN, Flags.FLAG8, Service.LOC_SRV, Service.INGRES_NET, Service.EMFIS_CNTL, Service.EMFIS_DATA, 147, 146, 153, 152, 159, 158, 165, 164, 171, 170, 177, 176, 183, 182, 189, 188, 195, 194, 201, AMQP.REPLY_SUCCESS, 816, -3}, new int[]{Service.LOCUS_MAP, 124, Service.CISCO_TNA, Service.CISCO_FNA, Service.NETBIOS_NS, Service.PROFILE, 143, Service.BL_IDM, 149, 148, 155, 154, 161, 160, 167, 166, 173, 172, 179, 178, 185, 184, 191, 190, 197, 196, 203, 202, 818, 817}, new int[]{283, 282, 277, 276, 271, 270, 265, 264, 259, 258, Type.MAILB, Type.AXFR, 247, 246, 241, 240, 235, 234, 229, 228, 223, 222, 217, 216, 211, 210, 205, 204, 819, -3}, new int[]{285, 284, 279, 278, 273, 272, 267, 266, 261, 260, Type.ANY, Type.MAILA, Type.TKEY, 248, Service.SUR_MEAS, 242, 237, 236, 231, 230, 225, 224, 219, 218, 213, 212, 207, AMQP.FRAME_END, 821, 820}, new int[]{287, 286, 281, 280, 275, 274, 269, 268, 263, 262, InputDeviceCompat.SOURCE_KEYBOARD, KEYRecord.OWNER_ZONE, Type.IXFR, Type.TSIG, Service.LINK, 244, 239, 238, 233, 232, 227, 226, 221, 220, 215, 214, 209, 208, 822, -3}, new int[]{289, 288, 295, 294, 301, 300, 307, 306, AMQP.NO_CONSUMERS, AMQP.NO_ROUTE, 319, 318, 325, 324, 331, 330, 337, 336, 343, 342, 349, 348, 355, 354, 361, 360, 367, 366, 824, 823}, new int[]{291, 290, 297, 296, 303, 302, 309, 308, 315, 314, 321, AMQP.CONNECTION_FORCED, 327, 326, 333, 332, 339, 338, 345, 344, 351, 350, 357, 356, 363, 362, 369, 368, 825, -3}, new int[]{293, 292, 299, 298, 305, 304, AMQP.CONTENT_TOO_LARGE, 310, 317, 316, 323, 322, 329, 328, 335, 334, 341, 340, 347, 346, 353, 352, 359, 358, 365, 364, 371, 370, 827, 826}, new int[]{409, 408, AMQP.ACCESS_REFUSED, AMQP.INVALID_PATH, 397, 396, 391, 390, 79, 78, -2, -2, 13, 12, 37, 36, 2, -1, 44, 43, Service.POP_2, 108, 385, 384, 379, 378, 373, 372, 828, -3}, new int[]{411, 410, AMQP.RESOURCE_LOCKED, AMQP.NOT_FOUND, 399, 398, 393, 392, 81, 80, 40, -2, 15, 14, 39, 38, 3, -1, -1, 45, Service.SUNRPC, Recover.INDEX, 387, 386, 381, 380, 375, 374, 830, 829}, new int[]{413, 412, 407, AMQP.PRECONDITION_FAILED, 401, 400, 395, 394, 83, 82, 41, -3, -3, -3, -3, -3, 5, 4, 47, 46, Service.AUTH, 112, 389, 388, 383, 382, 377, 376, 831, -3}, new int[]{415, 414, 421, 420, 427, 426, Service.X400, Service.ISO_TSAP, 55, 54, 16, -3, -3, -3, -3, -3, -3, -3, 20, 19, 85, 84, 433, 432, 439, 438, 445, 444, 833, 832}, new int[]{417, 416, 423, 422, 429, 428, Service.CSNET_NS, Service.X400_SND, 57, 56, -3, -3, -3, -3, -3, -3, -3, -3, 22, 21, 87, 86, 435, 434, 441, 440, 447, 446, 834, -3}, new int[]{419, 418, 425, 424, 431, 430, Service.RTELNET, 106, 59, 58, -3, -3, -3, -3, -3, -3, -3, -3, -3, 23, 89, 88, 437, 436, WebSocket.DEFAULT_WSS_PORT, 442, 449, 448, 836, 835}, new int[]{481, 480, 475, 474, 469, 468, 48, -2, 30, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, 0, 53, 52, 463, 462, 457, 456, 451, 450, 837, -3}, new int[]{483, 482, 477, 476, 471, 470, 49, -1, -2, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -2, -1, 465, 464, 459, 458, 453, 452, 839, 838}, new int[]{485, 484, 479, 478, 473, 472, 51, 50, 31, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, 1, -2, 42, 467, 466, 461, 460, 455, 454, 840, -3}, new int[]{487, 486, 493, 492, 499, 498, 97, 96, 61, 60, -3, -3, -3, -3, -3, -3, -3, -3, -3, 26, 91, 90, AMQP.UNEXPECTED_FRAME, AMQP.CHANNEL_ERROR, 511, 510, 517, 516, 842, 841}, new int[]{489, 488, 495, 494, AMQP.FRAME_ERROR, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH, 99, 98, 63, 62, -3, -3, -3, -3, -3, -3, -3, -3, 28, 27, 93, 92, 507, AMQP.RESOURCE_ERROR, InputDeviceCompat.SOURCE_DPAD, KEYRecord.OWNER_HOST, 519, 518, 843, -3}, new int[]{491, 490, 497, 496, AMQP.COMMAND_INVALID, AMQP.SYNTAX_ERROR, Service.HOSTNAME, 100, 65, 64, 17, -3, -3, -3, -3, -3, -3, -3, 18, 29, 95, 94, 509, 508, 515, 514, 521, 520, 845, 844}, new int[]{559, 558, 553, 552, 547, 546, AMQP.INTERNAL_ERROR, AMQP.NOT_IMPLEMENTED, 73, 72, 32, -3, -3, -3, -3, -3, -3, 10, 67, 66, Service.SFTP, 114, 535, 534, 529, 528, 523, 522, 846, -3}, new int[]{561, 560, 555, 554, 549, 548, 543, 542, 75, 74, -2, -1, 7, 6, 35, 34, 11, -2, 69, 68, Service.UUCP_PATH, 116, 537, 536, 531, AMQP.NOT_ALLOWED, 525, 524, 848, 847}, new int[]{563, 562, 557, 556, 551, 550, 545, 544, 77, 76, -2, 33, 9, 8, 25, 24, -1, -2, 71, 70, Service.NNTP, 118, 539, 538, 533, 532, 527, 526, 849, -3}, new int[]{565, 564, 571, 570, 577, 576, 583, 582, 589, 588, 595, 594, 601, SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT, 607, 606, 613, 612, 619, 618, 625, 624, 631, 630, 637, 636, 643, 642, 851, 850}, new int[]{567, 566, 573, 572, 579, 578, 585, 584, 591, 590, 597, 596, 603, 602, 609, 608, 615, 614, 621, 620, 627, 626, 633, 632, 639, 638, 645, 644, 852, -3}, new int[]{569, 568, 575, 574, 581, 580, 587, 586, 593, 592, 599, 598, 605, 604, 611, 610, 617, 616, 623, 622, 629, 628, 635, 634, 641, 640, 647, 646, 854, 853}, new int[]{727, 726, 721, 720, 715, 714, 709, 708, 703, 702, 697, 696, 691, 690, 685, 684, 679, 678, 673, 672, 667, 666, 661, 660, 655, 654, 649, 648, 855, -3}, new int[]{729, 728, 723, 722, 717, 716, 711, 710, 705, 704, 699, 698, 693, 692, 687, 686, 681, 680, 675, 674, 669, 668, 663, 662, 657, 656, 651, 650, 857, 856}, new int[]{731, 730, 725, 724, 719, 718, 713, 712, 707, 706, 701, Constants.ANIMATION_DURATION, 695, 694, 689, 688, 683, 682, 677, 676, 671, 670, 665, 664, 659, 658, 653, 652, 858, -3}, new int[]{733, 732, 739, 738, 745, 744, 751, 750, 757, 756, 763, 762, 769, Flags.OWNER_MASK, 775, 774, 781, 780, 787, 786, 793, 792, 799, 798, 805, 804, 811, 810, 860, 859}, new int[]{735, 734, 741, 740, 747, 746, 753, 752, 759, 758, 765, 764, 771, 770, 777, 776, 783, 782, 789, 788, 795, 794, 801, 800, 807, 806, 813, 812, 861, -3}, new int[]{737, 736, 743, 742, 749, 748, 755, 754, 761, 760, 767, 766, 773, 772, 779, 778, 785, 784, 791, 790, 797, 796, 803, 802, 809, 808, 815, 814, 863, 862}};
    }

    BitMatrixParser(BitMatrix bitMatrix) {
        this.bitMatrix = bitMatrix;
    }

    byte[] readCodewords() {
        byte[] result = new byte[144];
        int height = this.bitMatrix.getHeight();
        int width = this.bitMatrix.getWidth();
        int y = 0;
        while (y < height) {
            int[] bitnrRow = BITNR[y];
            int x = 0;
            while (x < width) {
                int bit = bitnrRow[x];
                if (bit >= 0 && this.bitMatrix.get(x, y)) {
                    int i = bit / 6;
                    result[i] = (byte) (result[i] | ((byte) (1 << (5 - (bit % 6)))));
                }
                x++;
            }
            y++;
        }
        return result;
    }
}
