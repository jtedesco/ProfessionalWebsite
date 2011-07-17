__author__ = 'Jon Tedesco'


def common_styling():
    """
      Returns the 'style' tag containing the common styling information
    """

    return """
               <style>
                #search_form_div {
                    position: absolute;
                    top: 250px;
                    width: 100%;
                    z-index: 100;
                }

                #search_form {
                    display:block;
                    margin:0 auto;
                    background:none;
                    width: 600px;
                }

                .invisible_table {
                    border: 0;
                }

                .full_cell {
                    width: 100%;
                }

                .search_box {
                    width: 100%;
                    border-bottom: 1px solid #e7e7e7;
                    padding: 8px 0 0;
                    position: relative
                }

                .search_buttons {
                    border-top:1px solid #ccc
                }
                </style>
                <style>
                body {
                    margin: 0
                }

                #gog {
                    padding: 3px 8px 0
                }

                .gac_m td {
                    line-height: 17px
                }

                form {
                    margin-bottom: 20px
                }

                body, td, a, p, .h {
                    font-family: arial, sans-serif
                }

                .ts td {
                    padding: 0
                }

                em {
                    font-weight: bold;
                    font-style: normal
                }

                .lst {
                    width: 496px
                }

                input {
                    font-family: inherit
                }

                body {
                    background: #fff;
                    color: black
                }

                input {
                    -moz-box-sizing: content-box
                }

                a {
                    color: #11c;
                    text-decoration: none
                }

                a:hover, a:active {
                    text-decoration: underline
                }

                .fl a {
                    color: #4272db
                }

                a:visited {
                    color: #551a8b
                }

                a.gb1, a.gb4 {
                    text-decoration: underline
                }

                a.gb3:hover {
                    text-decoration: none
                }

                #ghead a.gb2:hover {
                    color: #fff !important
                }

                .ds {
                    display: inline-block;
                }

                span.ds {
                    border-bottom: solid 1px #e7e7e7;
                    border-right: solid 1px #e7e7e7;
                    margin: 3px 0 4px;
                    margin-left: 4px
                }

                .sblc {
                    padding-top: 5px
                }

                .sblc a {
                    display: block;
                    margin: 2px 0;
                    margin-left: 13px;
                    font-size: 11px;
                }

                .lsbb {
                    background: #eee;
                    border: solid 1px;
                    border-color: #ccc #999 #999 #ccc;
                    height: 30px;
                    display: block
                }

                .lsb {
                    background: url(http://www.google.com/images/nav_logo28.png) bottom;
                    font: 15px arial, sans-serif;
                    border: none;
                    color: #000;
                    cursor: pointer;
                    height: 30px;
                    margin: 0;
                    outline: 0;
                    vertical-align: top
                }

                .lsb:active {
                    background: #ccc
                }

                .lst:focus {
                    outline: none
                }

                .ftl, #fll a {
                    margin: 0 12px
                }

                #addlang a {
                    padding: 0 3px
                }

                .gac_v div {
                    display: none
                }

                .gac_v .gac_v2, .gac_bt {
                    display: block !important
                }

                body, html {
                    font-size: small
                }

                h1, ol, ul, li {
                    margin: 0;
                    padding: 0
                }

                .nojsb {
                    display: none
                }

                .nojsv {
                    visibility: hidden
                }

                #body, #footer {
                    display: block
                }

                #po-bar {
                    margin-bottom: 4px;
                    padding-top: 1px;
                    white-space: nowrap;
                    z-index: 98
                }

                #po-bar a, #po-bar a:visited {
                    color: #4273db
                }

                #po-bar .flt {
                    cursor: pointer;
                    padding-bottom: 0;
                    text-decoration: none
                }

                #po-box a {
                    display: block;
                    padding: .2em .31em;
                    text-decoration: none
                }

                #po-box a:hover {
                    background: #558be3;
                    color: #fff !important;
                    text-decoration: none !important
                }

                #po-sc-lm a {
                    display: inline;
                    white-space: nowrap;
                    padding: 0
                }

                .po-selected .mark {
                    display: inline
                }

                .po-unselected .mark {
                    visibility: hidden
                }

                #ss-box a {
                    display: block;
                    padding: .2em .31em;
                    text-decoration: none
                }

                #ss-box a:hover {
                    background: #558be3;
                    color: #fff !important
                }

                a.ss-selected {
                    color: #000 !important;
                    font-weight: bold
                }

                a.ss-unselected {
                    color: #4273db !important
                }

                .ss-selected .mark {
                    display: inline
                }

                .ss-unselected .mark {
                    visibility: hidden
                }

                #ss-barframe {
                    background: #fff;
                    left: 0;
                    position: absolute;
                    visibility: hidden;
                    z-index: 100
                }

                body {
                    overflow-y: scroll
                }

                #logo {
                    color: #fff;
                    display: block;
                    height: 62px;
                    margin: 3px 0 0 2px;
                    overflow: hidden;
                    position: relative;
                    width: 178px
                }

                #logo img {
                    border: 0;
                    left: -0px;
                    position: absolute;
                    top: -145px
                }

                input {
                    -moz-box-sizing: content-box
                }

                .lst-b, .lst {
                    height: 26px;
                    padding: 4px 0 0
                }

                .tia input {
                    border-right: none;
                    padding-right: 0px
                }

                .lst-b {
                    border-right: none
                }

                .lst {
                    -moz-box-sizing: content-box;
                    background: #fff;
                    color: #000;
                    font: 17px arial, sans-serif;
                    float: left;
                    padding-left: 6px;
                    padding-right: 10px;
                    vertical-align: top;
                    width: 100%
                }

                .lst-td {
                    background: #fff;
                    border-bottom: 1px solid #999
                }

                .lst-td-xbtn {
                    border-top: 1px solid #ccc;
                    padding-right: 16px
                }

                .ds {
                    border-right: 1px solid #e7e7e7;
                    position: relative;
                    height: 32px;
                    z-index: 100
                }

                .lsbb {
                    background: #eee;
                    border: 1px solid #999;
                    border-top-color: #ccc;
                    border-left-color: #ccc;
                    height: 30px
                }

                .lsb {
                    font: 15px arial, sans-serif;
                    background-position: bottom;
                    border: none;
                    color: #000;
                    cursor: pointer;
                    height: 30px;
                    margin: 0;
                    vertical-align: top
                }

                .lsb:active {
                    background: #ccc
                }

                .lst:focus {
                    outline: none
                }

                .lsd {
                    font-size: 11px;
                    position: absolute;
                    top: 3px;
                    left: 16px
                }

                .gl {
                    white-space: nowrap
                }

                #searchform {
                    position: absolute;
                    top: 250px;
                    width: 100%;
                    z-index: 100
                }

                .tsf-p, .ctr-p > center {
                    padding-left: 205px;
                    padding-right: 22%
                }

                #search_form, .ctr-p {
                    margin: 0 auto;
                    max-width: 1181px;
                    min-width: 817px
                }

                .fade #center_col, .fade #rhs, .fade #leftnav {
                    filter: alpha(opacity = 33.3);
                    opacity: 0.333
                }

                #misspell {
                    background: transparent;
                    color: #fff;
                    position: absolute;
                    z-index: 2
                }

                .misspelled {
                    background: url('http://www.google.com/images/experiments/wavy-underline.png') 50% 100% repeat-x;
                    color: #fff;
                    display: inline-block;
                    line-height: 1.05em;
                    padding: 0 0 3px 0
                }

                form {
                    display: inline
                }

                input[name="q"] {
                    background: transparent;
                    position: relative;
                    z-index: 3
                }

                #grey {
                    background: #fff;
                    border-color: transparent;
                    color: silver;
                    overflow: hidden;
                    position: absolute;
                    z-index: 1
                }

                #xbtn {
                    color: #a1b9ed;
                    cursor: pointer;
                    display: none;
                    font: 28px Arial, sans-serif;
                    height: 20px;
                    line-height: 28px;
                    margin: 4px -8px 4px 0;
                    padding: 0;
                    top: 2px;
                    width: 14px;
                    z-index: 4
                }

                #xbtn:hover, .flt, .flt u, a.fl {
                    color: #4272db;
                    text-decoration: none
                }

                .flt:hover, .flt:hover u, a.fl:hover {
                    text-decoration: underline
                }

                .lst {
                    border-width: 0 0 0 1px
                }

                #sfopt a:hover {
                    text-decoration: none
                }

                #sfopt a.flt:hover {
                    text-decoration: underline
                }

                #pocs {
                    background: #fff1a8;
                    color: #000;
                    font-size: 10pt;
                    margin: 0;
                    padding: 6px 7px
                }

                #pocs.sft {
                    background: #fff;
                    color: #777
                }

                #pocs a {
                    color: #11c
                }

                #pocs.sft a {
                    color: #4373db
                }

                #pocs > div {
                    margin: 0;
                    padding: 0
                }

                #knavm {
                    color: #4273db;
                    display: inline;
                    font: 11px arial, sans-serif !important;
                    left: -13px;
                    position: absolute;
                    top: 2px;
                    z-index: 2
                }

                #pnprev #knavm {
                    bottom: 1px;
                    top: auto
                }

                #pnnext #knavm {
                    bottom: 1px;
                    left: 40px;
                    top: auto
                }

                a.noline {
                    outline: 0
                }
                </style>
           """


def search_result_styling():
    """
      This function returns the style tag for the search results
    """

    return """

        <script language="JavaScript" type="text/javascript">
        <!--
        function correctspelling ( query )
        {
          document.f.q.value = query;
          document.f.submit() ;
        }
        -->
        </script>
        <style>
            body {
                color: #000;
                margin: 3px 0;
                overflow-y: scroll
            }

            body, #leftnav, #tbd, #atd, #tsf, #hidden_modes, #hmp {
                background: #fff
            }

            #gog {
                background: #fff
            }

            #gbar, #guser {
                font-size: 13px;
                padding-top: 1px !important
            }

            #gbar {
                float: left;
                height: 22px
            }

            #guser {
                padding-bottom: 7px !important;
                text-align: right
            }

            .gbh, .gbd {
                border-top: 1px solid #c9d7f1;
                font-size: 1px
            }

            .gbh {
                height: 0;
                position: absolute;
                top: 24px;
                width: 100%
            }

            #gbs, .gbm {
                background: #fff;
                left: 0;
                position: absolute;
                text-align: left;
                visibility: hidden;
                z-index: 1000
            }

            .gbm {
                border: 1px solid;
                border-color: #c9d7f1 #36c #36c #a2bae7;
                z-index: 1001
            }

            .gb1 {
                margin-right: .5em
            }

            .gb1, .gb3 {
                zoom: 1
            }

            .gb2 {
                display: block;
                padding: .2em .5em
            }

            .gb2, .gb3 {
                text-decoration: none;
                border-bottom: none
            }

            a.gb1, a.gb2, a.gb3, a.gb4 {
                color: #00c !important
            }

            a.gb2:hover {
                background: #36c;
                color: #fff !important
            }

            a.gb1, a.gb2, a.gb3, .link {
                color: #2200C1 !important
            }

            .ts {
                border-collapse: collapse
            }

            .ts td {
                padding: 0
            }

            .ti, .bl, #res h3 {
                display: inline
            }

            .ti {
                display: inline-table
            }

            #tads a.mblink, #tads a.mblink b, #tadsc a.mblink, #tadsc a.mblink b, #rhs a.mblink, #rhs a.mblink b {
                color: #2200C1 !important
            }

            a:link, .w, #prs a:visited, #prs a:active, .q:active, .q:visited {
                color: #2200C1
            }

            .mblink:visited, a:visited {
                color: #551a8b
            }

            .vst:link {
                color: #551a8b
            }

            .cur, .b {
                font-weight: bold
            }

            .j {
                width: 42em;
                font-size: 82%
            }

            .s {
                max-width: 42em
            }

            .sl {
                font-size: 82%
            }

            #gb {
                text-align: right;
                padding: 1px 0 7px;
                margin: 0
            }

            .hd {
                position: absolute;
                width: 1px;
                height: 1px;
                top: -1000em;
                overflow: hidden
            }

            .gl, .f, .m, .c h2, #mbEnd h2, #tads h2, #tadsc h2, .descbox {
                color: #767676
            }

            .a, cite, cite a:link, cite a:visited, .cite, .cite:link, #mbEnd cite b, #tads cite b, #tadsc cite b {
                color: #0e774a;
                font-style: normal
            }

            .ng {
                color: #c11
            }

            h1, ol, ul, li {
                margin: 0;
                padding: 0
            }

            li.g, body, html, .std, .c h2, #mbEnd h2, h1 {
                font-size: small;
                font-family: arial, sans-serif
            }

            .c h2, #mbEnd h2, h1 {
                font-weight: normal
            }

            .clr {
                clear: both;
                margin: 0 8px
            }

            .blk a {
                color: #000
            }

            #nav a {
                display: block
            }

            #nav .i {
                color: #a90a08;
                font-weight: bold
            }

            .csb, .ss, #logo span, .play_icon, .mini_play_icon, .micon, .licon, .close_btn, #tbp, .lsb, .mbi {
                background: url(http://www.generationschicago.com/temp/pygoogle_nav_logo.jpg) no-repeat;
                overflow: hidden
            }

            .csb, .ss {
                background-position: 0 0;
                height: 40px;
                display: block
            }

            .ss {
                background-position: 0 -41px;
                position: absolute;
                left: 0;
                top: 0
            }

            .cps {
                height: 18px;
                overflow: hidden;
                width: 114px
            }

            .mbi {
                background-position: -159px -41px;
                display: inline-block;
                float: left;
                height: 13px;
                margin-right: 3px;
                margin-top: 1px;
                width: 13px
            }

            #nav td {
                padding: 0;
                text-align: center
            }

            #logo {
                display: block;
                overflow: hidden;
                position: relative;
                width: 160px;
                height: 56px;
                margin: 7px 0 0 -3px
            }

            #logo img {
                border: none;
                position: absolute;
                left: -0px;
                top: -145px
            }

            .ws, .wsa, .wxs, .wpb {
                background: url(http://www.google.com/images/nav_logo29.png) no-repeat;
                border: 0;
                cursor: pointer;
                display: none;
                margin-right: 3px;
                height: 0px;
                vertical-align: bottom;
                width: 0px
            }

            .ws, .wsa {
                display: inline;
                height: 14px;
                margin-left: 5px;
                vertical-align: 0;
                width: 14px
            }

            .ws {
                background-position: -129px -70px
            }

            .wsa {
                background-position: -114px -70px
            }

            .wpb {
                background-position: -159px -41px;
                display: inline;
                height: 13px;
                vertical-align: -2px;
                width: 13px
            }

            .link {
                cursor: pointer
            }

            #logo span, .ch {
                cursor: pointer
            }

            h3, .med {
                font-size: medium;
                font-weight: normal;
                padding: 0;
                margin: 0
            }

            .e {
                margin: 2px 0 .75em
            }

            .slk div {
                padding-left: 12px;
                text-indent: -10px
            }

            .fc {
                margin-top: .5em;
                padding-left: 16px
            }

            #mbEnd cite {
                display: block;
                text-align: left
            }

            #rhs_block {
                margin-bottom: -20px
            }

            #bsf, .blk {
                border-top: 1px solid #6b90da;
                background: #f0f7f9
            }

            #bsf {
                border-bottom: 1px solid #6b90da
            }

            #cnt {
                clear: both
            }

            #res {
                padding-right: 1em;
                margin: 0 16px
            }

            .c {
                background: #fbf0fa;
                margin: 0 8px
            }

            .c li {
                padding: 0 3px 0 8px;
                margin: 0
            }

            #mbEnd li {
                margin: 1em 0;
                padding: 0
            }

            .xsm {
                font-size: x-small
            }

            ol li {
                list-style: none
            }

            #ncm ul li {
                list-style-type: disc
            }

            .sm li {
                margin: 0
            }

            .gl, #foot a, .nobr {
                white-space: nowrap
            }

            #mbEnd .med {
                white-space: normal
            }

            .sl, .r {
                display: inline;
                font-weight: normal;
                margin: 0
            }

            .r {
                font-size: medium
            }

            h4.r {
                font-size: small
            }

            .mr {
                margin-top: 6px
            }

            h3.tbpr {
                margin-top: .4em;
                margin-bottom: 1.2em
            }

            img.tbpr {
                border: 0px;
                width: 15px;
                height: 15px;
                margin-right: 3px
            }

            .jsb {
                display: block
            }

            .nojsb {
                display: none
            }

            .nwd {
                font-size: 10px;
                padding: 16px;
                text-align: center
            }

            .rt1 {
                background: transparent url(http://www.google.com/images/bubble1.png) no-repeat
            }

            .rt2 {
                background: transparent url(http://www.google.com/images/bubble2.png) repeat 0 0 scroll
            }

            .sb {
                background: url(http://www.google.com/images/scrollbar.png) repeat scroll 0 0;
                cursor: pointer;
                width: 14px
            }

            .rtdm:hover {
                text-decoration: underline
            }

            #rtr .g {
                margin: 1em 0
            }

            #ss-box {
                background: #fff;
                border: 1px solid;
                border-color: #c9d7f1 #36c #36c #a2bae7;
                left: 0;
                margin-top: .1em;
                position: absolute;
                visibility: hidden;
                z-index: 101
            }

            #ss-box a {
                display: block;
                padding: .2em .31em;
                text-decoration: none
            }

            #ss-box a:hover {
                background: #558be3;
                color: #fff !important
            }

            a.ss-selected {
                color: #000 !important;
                font-weight: bold
            }

            a.ss-unselected {
                color: #4273db !important
            }

            .ss-selected .mark {
                display: inline
            }

            .ss-unselected .mark {
                visibility: hidden
            }

            #ss-barframe {
                background: #fff;
                left: 0;
                position: absolute;
                visibility: hidden;
                z-index: 100
            }

            .ri_cb {
                left: 0;
                margin: 6px;
                position: absolute;
                top: 0;
                z-index: 1
            }

            .ri_sp {
                display: inline-block;
                text-align: center;
                vertical-align: top;
                margin-bottom: 6px
            }

            .ri_sp img {
                vertical-align: bottom
            }

            #vsrs, #vspci, .vspib {
                background: url(http://www.google.com/images/nav_logo29.png) no-repeat
            }

            #vspb {
                -webkit-box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.5);
                background-color: #fff;
                border: 1px solid #999;
                display: block;
                padding: 15px;
                position: absolute;
                top: 0;
                visibility: hidden;
                z-index: 110
            }

            #vsrs {
                background-position: 0 -208px;
                height: 30px;
                left: -16px;
                position: absolute;
                width: 17px
            }

            #vspc {
                background-color: #fff;
                border: none;
                padding: 0;
                position: absolute;
                top: 15px
            }

            #vspci {
                background-position: -160px -121px;
                border-bottom: 5px solid white;
                border-left: 5px solid white;
                cursor: pointer;
                height: 16px;
                right: 7px;
                top: 7px;
                position: absolute;
                width: 16px;
                z-index: 16
            }

            #vsic {
                overflow: hidden;
                z-index: -1
            }

            #vsli {
                border: none;
                display: block;
                left: 139px;
                position: absolute;
                top: 45%;
                visibility: hidden
            }

            #vsm {
                font-size: 16px;
                left: 0;
                overflow: hidden;
                padding: 10px;
                position: absolute;
                right: 0;
                text-align: center;
                top: 35%
            }

            #vsi, .vsi {
                border: none;
                display: block
            }

            #vsia {
                text-decoration: none
            }

            .vsbb {
                background-color: rgba(255, 245, 64, 0.2);
                border: 4px solid #ff7f27;
                opacity: 0.8;
                position: absolute;
                z-index: 14
            }

            .vstb {
                -webkit-box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.4);
                background-color: #fff;
                border: 1px solid #ff7f27;
                color: #000;
                font-size: 12px;
                left: -4px;
                overflow: hidden;
                padding: 5px;
                position: absolute;
                right: -4px;
                text-overflow: ellipsis;
                z-index: 15
            }

            a .vstb em, a .vstb b {
                text-decoration: none
            }

            div.vsc {
                display: inline-block;
                position: relative;
                width: 100%;
                z-index: 1
            }

            .vspi {
                bottom: -5px;
                left: -8px;
                position: absolute;
                right: -8px;
                top: -5px;
                z-index: -1
            }

            .vscl .vspi {
                left: -6px
            }

            .vsgv .vspi {
                right: 7px;
                top: 1px
            }

            .nulead .vspi {
                left: -6px;
                top: -3px;
                bottom: -4px
            }

            .nusec .vspi {
                left: -6px;
                top: -4px;
                bottom: -4px
            }

            .vspib {
                background-position: -19px -209px;
                border: 0;
                cursor: pointer;
                display: inline;
                height: 13px;
                margin-left: 5px;
                margin-right: 3px;
                vertical-align: 0;
                width: 13px
            }

            .vsgv .vspib {
                margin-top: 2px
            }

            .vsc .ws, .vsc .wsa {
                margin-right: 0
            }

            div.vsc:hover .vspib, #ires.vse .vspib {
                background-position: -35px -209px
            }

            #ires.vsh div.vsc:hover>.vspi, div.vso>.vspi {
                background: #ebf2fc;
                border: 1px solid #cddcf9
            }

            div.vsc:hover #nqsbq {
                background-color: #fff
            }

            div.fade>#center_col div.vsc:hover>.vspi {
                background: none !important;
                border: none !important
            }

            #gog {
                background: none
            }

            .tl {
                display: inline-block;
                position: relative
            }

            .mbl {
                margin-top: 7px
            }

            .popup-cont {
                background: #fff;
                border: 1px solid #ccc;
                display: none;
                padding: 15px;
                position: absolute;
                visibility: hidden;
                width: 450px;
                z-index: 1000
            }

            .popup-display {
                display: inline
            }

            .popup-close {
                background: transparent url(http://www.google.com/products/images/popup-sprite.png) no-repeat -90px 0;
                cursor: pointer;
                height: 12px;
                overflow: hidden;
                position: absolute;
                right: 10px;
                top: 10px;
                width: 12px
            }

            .popup-title {
                font-size: 124%;
                font-weight: bold;
                padding-bottom: 3px
            }

            .ps-map {
                float: left
            }

            .popup-arrow {
                background: transparent url(http://www.google.com/products/images/popup-sprite.png) no-repeat 0 0;
                overflow: hidden;
                position: absolute
            }

            .popup-arrow-top {
                background-position: -46px -23px;
                height: 23px;
                left: 20px;
                top: -23px;
                width: 44px
            }

            .popup-arrow-bottom {
                background-position: -46px 0;
                bottom: -23px;
                height: 23px;
                left: 20px;
                width: 44px
            }

            .popup-arrow-bottomright {
                background-position: -46px 0;
                bottom: -23px;
                height: 23px;
                right: 20px;
                width: 44px
            }

            .popup-arrow-left {
                height: 44px;
                left: -23px;
                top: 20px;
                width: 23px
            }

            .popup-arrow-right {
                background-position: -23px 0;
                height: 44px;
                right: -23px;
                top: 20px;
                width: 23px
            }

            .ps-map img {
                border: 1px solid #00c
            }

            a.tiny-pin, a.tiny-pin:link, a.tiny-pin:hover {
                text-decoration: none;
                color: #4272DB
            }

            a.tiny-pin:hover span {
                text-decoration: underline
            }

            .tiny-pin table {
                vertical-align: middle
            }

            .tiny-pin p {
                background-image: url(http://www.google.com/images/nav_logo29.png);
                background-position: -71px -60px;
                height: 15px;
                margin: 0;
                padding: 0;
                top: -1px;
                overflow: hidden;
                position: relative;
                width: 9px;
            }

            .loc-list-cont {
                margin-left: 200px
            }

            body .locations-table td {
                background: #fff;
                padding: 3px;
                vertical-align: top
            }

            .pspa-price {
                font-size: medium;
                font-weight: bold
            }

            .pspa-call-price {
                font-size: small;
                font-weight: bold
            }

            .pspa-store-avail {
                color: #282
            }

            .pspa-out-of-stock {
                color: #c11
            }

            .mbl {
                margin: 1em 0 0
            }

            em {
                font-weight: bold;
                font-style: normal
            }

            li.w0 .ws, td.w0 .ws {
                opacity: 0.5
            }

            li.w0:hover .ws, td.w0:hover .ws {
                opacity: 1
            }

            ol, ul, li {
                border: 0;
                margin: 0;
                padding: 0
            }

            li {
                line-height: 1.2
            }

            li.g {
                margin-top: 0;
                margin-bottom: 14px
            }

            .ibk, #productbox .fmg {
                display: inline-block;
                *display: inline;
                vertical-align: top;
                zoom: 1
            }

            .tsw {
                width: 595px
            }

            #cnt {
                margin-left: 14px;
                min-width: 817px;
                max-width: 1181px;
                margin: 0 auto;
                padding-top: 17px;
            }

            .gbh {
                top: 24px
            }

            #gbar {
                margin-left: 8px;
                height: 20px
            }

            #guser {
                margin-right: 8px;
                padding-bottom: 5px !important
            }

            .mbi {
                margin-bottom: -1px
            }

            .uc {
                margin-left: 163px
            }

            #center_col, #foot {
                margin-left: 173px;
                margin-right: 254px;
                padding: 0 8px 0 8px
            }

            #subform_ctrl {
                font-size: 11px;
                margin-left: 190px;
                margin-right: 254px;
                min-height: 26px;
                padding-top: 3px;
                padding-right: 8px;
            }

            #center_col {
                border-left: 1px solid #d3e1f9;
                clear: both
            }

            #brs p {
                margin: 0;
                padding-top: 5px
            }

            .brs_col {
                display: inline-block;
                float: left;
                font-size: small;
                white-space: nowrap;
                padding-right: 16px;
                margin-top: -1px;
                padding-bottom: 1px
            }

            #tads, #tadsc, #tadsb {
                margin-bottom: 8px !important
            }

            #tads li, #tadsc li, #tadsb li {
                padding: 1px 0
            }

            #tads li.taf, #tadsb li.taf {
                padding: 1px 0 0
            }

            #tads li.tam, #tadsb li.tam {
                padding: 14px 0 0
            }

            #tads li.tal, #tadsb li.tal {
                padding: 14px 0 1px
            }

            #res {
                border: 0;
                margin: 0;
                padding: 4px 8px 0
            }

            #ires {
                padding-top: 1px
            }

            .mbl {
                margin-top: 5px
            }

            #leftnav li {
                display: block
            }

            .micon, .licon, .close_btn {
                border: 0
            }

            #leftnav h2 {
                font-size: small;
                color: #767676;
                font-weight: normal;
                margin: 8px 0 0;
                padding-left: 8px;
                width: 147px
            }

            #tbbc dfn {
                padding: 4px
            }

            #tbbc.bigger .std {
                font-size: 154%;
                font-weight: bold;
                text-decoration: none
            }

            .close_btn {
                background-position: -144px -55px;
                float: right;
                height: 14px;
                width: 14px
            }

            .videobox {
                padding-bottom: 3px
            }

            #leftnav a {
                text-decoration: none
            }

            #leftnav a:hover {
                text-decoration: underline
            }

            .mitem, #showmodes {
                font-size: 15px;
                line-height: 24px;
                margin-right: 4px;
                padding-left: 8px
            }

            .mitem {
                margin-bottom: 2px
            }

            .mitem .q {
                display: block
            }

            .msel {
                height: 22px;
                padding-bottom: 2px;
                margin-bottom: 0;
                font-weight: bold
            }

            .micon {
                float: left;
                height: 19px;
                margin-top: 2px;
                margin-right: 5px;
                outline: none;
                padding-right: 1px;
                width: 19px
            }

            #showmodes .micon {
                background-position: -160px -103px;
                height: 17px;
                margin-left: 1px;
                margin-right: 6px;
                width: 17px
            }

            .licon {
                background-position: -159px -70px;
                float: left;
                height: 14px;
                margin-right: 3px;
                width: 14px
            }

            .open #showmodes .micon {
                background-position: -160px -85px
            }

            .open .msm, .msl {
                display: none
            }

            .open .msl {
                display: inline
            }

            .open #hidden_modes, .open #hmp {
                display: block
            }

            #swr li {
                border: 0;
                font-size: 13px;
                line-height: 1.2;
                margin: 0 0 4px;
                margin-right: 8px;
            }

            #tbd, #atd {
                display: block;
                margin-top: 8px;
                min-height: 1px
            }

            .tbt {
                font-size: 13px;
                line-height: 1.2
            }

            .tbou, .tbos, .tbots, .tbotu {
                margin-right: 8px;
                padding-left: 16px;
                padding-bottom: 3px;
                text-indent: -8px
            }

            .tbos, .tbots {
                font-weight: bold
            }

            #leftnav .tbots a {
                color: #000 !important;
                cursor: default;
                text-decoration: none
            }

            .tbfo .tbt, .tbpd {
                margin-bottom: 8px
            }

            #season_ {
                margin-top: 8px
            }

            #iszlt_sel.tbcontrol_vis {
                margin-left: 0
            }

            .tbpc, .tbpo, .lcsc, .lcso {
                font-size: 13px
            }

            .tbpc, .tbo .tbpo, .lco .lcsc {
                display: inline
            }

            .tbo .tbpc, .tbpo, .lco .lcso, .lcsc, #set_location_section {
                display: none
            }

            .lco #set_location_section {
                display: block
            }

            .lcot {
                margin: 0 8px;
            }

            .tbo #tbp, .lco .licon, .obsmo #obsmti {
                background-position: -144px -70px !important
            }

            #prc_opt label, #prc_ttl {
                display: block;
                font-weight: normal;
                margin-right: 2px;
                white-space: nowrap
            }

            #cdr_opt, #loc_opt, #prc_opt {
                padding-left: 8px;
                text-indent: 0
            }

            .tbou #cdr_frm, .tbou #cloc_frm {
                display: none
            }

            #cdr_frm, #cdr_min, #cdr_max {
                width: 88%
            }

            #cdr_opt label {
                display: block;
                font-weight: normal;
                margin-right: 2px;
                white-space: nowrap
            }

            .rhss {
                margin: 0 0 32px;
                margin-left: 8px
            }

            #mbEnd {
                margin: 0 0 32px;
                margin-left: 8px
            }

            #mbEnd h2 {
                color: #767676
            }

            #mbEnd li {
                margin: 12px 8px 0 0
            }

            a:link, .w, .q:active, .q:visited, .tbotu {
                color: #2200C1;
                cursor: pointer
            }

            a.fl:link, .fl a, .flt, a.flt, .gl a:link, a.mblink, .mblink b {
                color: #4272db
            }

            .osl a, .gl a, #tsf a, a.mblink, a.gl, a.fl, .slk a, .bc a, .flt, a.flt u, .oslk a {
                text-decoration: none
            }

            .osl a:hover, .gl a:hover, #tsf a:hover, a.mblink:hover, a.gl:hover, a.fl:hover, .slk a:hover, .bc a:hover, .flt:hover, a.flt:hover u, .oslk a:hover, .tbotu:hover {
                text-decoration: underline
            }

            #ss-box a:hover {
                text-decoration: none
            }

            #tads .mblink, #tadsc .mblink, #rhs .mblink {
                text-decoration: underline
            }

            .hpn, .osl {
                color: #767676
            }

            div#gbi, div#gbg {
                border-color: #a2bff0 #558be3 #558be3 #a2bff0
            }

            div#gbi a.gb2:hover, div#gbg a.gb2:hover, .mi:hover {
                background-color: #558be3
            }

            #guser a.gb2:hover, .mi:hover, .mi:hover * {
                color: #fff !important
            }

            #guser {
                color: #000
            }

            #imagebox_big img {
                padding: 2px !important
            }

            #productbox .fmg {
                margin-top: 7px;
                padding-right: 8px;
                text-align: left
            }

            #foot .ftl {
                margin-right: 12px
            }

            #foot a.slink {
                text-decoration: none;
                color: #4272db
            }

            #fll a, #bfl a {
                color: #4272db;
                margin: 0 12px;
                text-decoration: none
            }

            #foot a:hover {
                text-decoration: underline
            }

            #foot a.slink:visited {
                color: #551a8b
            }

            #blurbbox_bottom {
                color: #767676
            }

            .nvs a {
                text-decoration: underline
            }

            .stp {
                margin: 7px 0 17px
            }

            .ssp {
                margin: .33em 0 17px
            }

            #mss {
                margin: .33em 0 0;
                padding: 0;
                display: table
            }

            .mss_col {
                display: inline-block;
                float: left;
                font-size: small;
                white-space: nowrap;
                padding-right: 16px;
            }

            #mss p {
                margin: 0;
                padding-top: 5px
            }

            #gsr a:active, a.fl:active, .fl a:active, .gl a:active {
                color: #c11
            }

            .obsmo #obsmtxt, #obsltxt {
                display: none
            }

            .obsmo #obsltxt {
                display: inline
            }

            #obsmtc a {
                text-decoration: none
            }

            #obsmtc a:hover {
                text-decoration: underline
            }

            #po-bar {
                margin-bottom: 4px;
                padding-top: 1px;
                white-space: nowrap;
                z-index: 98
            }

            #po-bar a, #po-bar a:visited {
                color: #4273db
            }

            #po-bar .flt {
                cursor: pointer;
                padding-bottom: 0;
                text-decoration: none
            }

            #po-status {
                float: left
            }

            #po-box {
                background: #fff;
                border: 1px solid;
                border-color: #c9d7f1 #36c #36c #a2bae7;
                left: 0;
                margin-top: .1em;
                position: absolute;
                z-index: 101
            }

            #po-box a {
                display: block;
                padding: .2em .31em;
                text-decoration: none
            }

            #po-box a:hover {
                background: #558be3;
                color: #fff !important;
                text-decoration: none !important
            }

            #po-sc-lm a {
                display: inline;
                white-space: nowrap;
                padding: 0
            }

            .po-lm {
                color: #aaa
            }

            a.po-selected {
                color: #000 !important;
                font-weight: bold
            }

            a.po-unselected {
                color: #4273db !important
            }

            .po-selected .mark {
                display: inline
            }

            .po-unselected .mark {
                visibility: hidden
            }

            .gbd {
                border-top: 1px solid #c9d7f1;
                font-size: 1px
            }

            .gb2 {
                padding: .2em .5em
            }

            body {
                overflow-y: scroll
            }

            #logo {
                color: #fff;
                display: block;
                height: 56px;
                margin: 7px 0 0 -3px;
                overflow: hidden;
                position: relative;
                width: 160px
            }

            #logo img {
                border: 0;
                left: -0px;
                position: absolute;
                top: -145px
            }

            input {
                -moz-box-sizing: content-box
            }

            .lst-b, .lst {
                height: 26px;
                padding: 4px 0 0
            }

            .tia input {
                border-right: none;
                padding-right: 0px
            }

            .lst-b {
                border-right: none
            }

            .lst {
                -moz-box-sizing: content-box;
                background: #fff;
                color: #000;
                font: 17px arial, sans-serif;
                float: left;
                padding-left: 6px;
                padding-right: 10px;
                vertical-align: top;
                width: 100%
            }

            .lst-td {
                background: #fff;
                border-bottom: 1px solid #999
            }

            .lst-td-xbtn {
                border-top: 1px solid #ccc;
                padding-right: 16px
            }

            .ds {
                border-right: 1px solid #e7e7e7;
                position: relative;
                height: 32px;
                z-index: 100
            }

            .lsbb {
                background: #eee;
                border: 1px solid #999;
                border-top-color: #ccc;
                border-left-color: #ccc;
                height: 30px
            }

            .lsb {
                font: 15px arial, sans-serif;
                background-position: bottom;
                border: none;
                color: #000;
                cursor: pointer;
                height: 30px;
                margin: 0;
                vertical-align: top
            }

            .lsb:active {
                background: #ccc
            }

            .lst:focus {
                outline: none
            }

            .lsd {
                font-size: 11px;
                position: absolute;
                top: 3px;
                left: 16px
            }

            .gl {
                white-space: nowrap
            }

            #searchform {
                position: absolute;
                top: 250px;
                width: 100%;
                z-index: 100
            }

            .tsf-p, .ctr-p > center {
                padding-left: 182px;
                padding-right: 254px
            }

            #tsf, .ctr-p {
                margin: 0 auto;
                max-width: 1181px;
                min-width: 817px
            }

            .fade #center_col, .fade #rhs, .fade #leftnav {
                filter: alpha(opacity = 33.3);
                opacity: 0.333
            }

            #misspell {
                background: transparent;
                color: #fff;
                position: absolute;
                z-index: 2
            }

            .misspelled {
                background: url('http://www.google.com/images/experiments/wavy-underline.png') 50% 100% repeat-x;
                color: #fff;
                display: inline-block;
                line-height: 1.05em;
                padding: 0 0 3px 0
            }

            form {
                display: inline
            }

            input[name="q"] {
                background: transparent;
                position: relative;
                z-index: 3
            }

            #grey {
                background: #fff;
                border-color: transparent;
                color: silver;
                overflow: hidden;
                position: absolute;
                z-index: 1
            }

            #xbtn {
                color: #a1b9ed;
                cursor: pointer;
                display: none;
                font: 28px Arial, sans-serif;
                height: 20px;
                line-height: 28px;
                margin: 4px -8px 4px 0;
                padding: 0;
                top: 2px;
                width: 14px;
                z-index: 4
            }

            #xbtn:hover, .flt, .flt u, a.fl {
                color: #4272db;
                text-decoration: none
            }

            .flt:hover, .flt:hover u, a.fl:hover {
                text-decoration: underline
            }

            .lst {
                border-width: 0 0 0 1px
            }

            #sfopt a:hover {
                text-decoration: none
            }

            #sfopt a.flt:hover {
                text-decoration: underline
            }

            #pocs {
                background: #fff1a8;
                color: #000;
                font-size: 10pt;
                margin: 0;
                padding: 6px 7px
            }

            #pocs.sft {
                background: #fff;
                color: #777
            }

            #pocs a {
                color: #11c
            }

            #pocs.sft a {
                color: #4373db
            }

            #pocs > div {
                margin: 0;
                padding: 0
            }

            #knavm {
                color: #4273db;
                display: inline;
                font: 11px arial, sans-serif !important;
                left: -13px;
                position: absolute;
                top: 2px;
                z-index: 2
            }

            #pnprev #knavm {
                bottom: 1px;
                top: auto
            }

            #pnnext #knavm {
                bottom: 1px;
                left: 40px;
                top: auto
            }

            a.noline {
                outline: 0
            }

            #searchform .jsb {
                display: none
            }

            #searchform .nojsb {
                display: block
            }

            body {
                margin: 0
            }

            #gog {
                padding: 3px 8px 0
            }

            .gac_m td {
                line-height: 17px
            }

            form {
                margin-bottom: 20px
            }

            body, td, a, p, .h {
                font-family: arial, sans-serif
            }

            .ts td {
                padding: 0
            }

            em {
                font-weight: bold;
                font-style: normal
            }

            .lst {
                width: 496px
            }

            input {
                font-family: inherit
            }

            body {
                background: #fff;
                color: black
            }

            input {
                -moz-box-sizing: content-box
            }

            a {
                color: #11c;
                text-decoration: none
            }

            a:hover, a:active {
                text-decoration: underline
            }

            .fl a {
                color: #4272db
            }

            a:visited {
                color: #551a8b
            }

            a.gb1, a.gb4 {
                text-decoration: underline
            }

            a.gb3:hover {
                text-decoration: none
            }

            #ghead a.gb2:hover {
                color: #fff !important
            }

            .ds {
                display: inline-block;
            }

            span.ds {
                border-bottom: solid 1px #e7e7e7;
                border-right: solid 1px #e7e7e7;
                margin: 3px 0 4px;
                margin-left: 4px
            }

            .sblc {
                padding-top: 5px
            }

            .sblc a {
                display: block;
                margin: 2px 0;
                margin-left: 13px;
                font-size: 11px;
            }

            .lsbb {
                background: #eee;
                border: solid 1px;
                border-color: #ccc #999 #999 #ccc;
                height: 30px;
                display: block
            }

            .lsb {
                background: url(http://www.google.com/images/nav_logo28.png) bottom;
                font: 15px arial, sans-serif;
                border: none;
                color: #000;
                cursor: pointer;
                height: 30px;
                margin: 0;
                outline: 0;
                vertical-align: top
            }

            .lsb:active {
                background: #ccc
            }

            .lst:focus {
                outline: none
            }

            .ftl, #fll a {
                margin: 0 12px
            }

            #addlang a {
                padding: 0 3px
            }

            .gac_v div {
                display: none
            }

            .gac_v .gac_v2, .gac_bt {
                display: block !important
            }

            body, html {
                font-size: small
            }

            h1, ol, ul, li {
                margin: 0;
                padding: 0
            }

            .nojsb {
                display: none
            }

            .nojsv {
                visibility: hidden
            }

            #body, #footer {
                display: block
            }

            #po-bar {
                margin-bottom: 4px;
                padding-top: 1px;
                white-space: nowrap;
                z-index: 98
            }

            #po-bar a, #po-bar a:visited {
                color: #4273db
            }

            #po-bar .flt {
                cursor: pointer;
                padding-bottom: 0;
                text-decoration: none
            }

            #po-box a {
                display: block;
                padding: .2em .31em;
                text-decoration: none
            }

            #po-box a:hover {
                background: #558be3;
                color: #fff !important;
                text-decoration: none !important
            }

            #po-sc-lm a {
                display: inline;
                white-space: nowrap;
                padding: 0
            }

            .po-selected .mark {
                display: inline
            }

            .po-unselected .mark {
                visibility: hidden
            }

            #ss-box a {
                display: block;
                padding: .2em .31em;
                text-decoration: none
            }

            #ss-box a:hover {
                background: #558be3;
                color: #fff !important
            }

            a.ss-selected {
                color: #000 !important;
                font-weight: bold
            }

            a.ss-unselected {
                color: #4273db !important
            }

            .ss-selected .mark {
                display: inline
            }

            .ss-unselected .mark {
                visibility: hidden
            }

            #ss-barframe {
                background: #fff;
                left: 0;
                position: absolute;
                visibility: hidden;
                z-index: 100
            }

            body {
                overflow-y: scroll
            }

            #logo {
                color: #fff;
                display: block;
                height: 62px;
                margin: 3px 0 0 2px;
                overflow: hidden;
                position: relative;
                width: 178px
            }

            #logo img {
                border: 0;
                left: -0px;
                position: absolute;
                top: -145px
            }

            input {
                -moz-box-sizing: content-box
            }

            .lst-b, .lst {
                height: 26px;
                padding: 4px 0 0
            }

            .tia input {
                border-right: none;
                padding-right: 0px
            }

            .lst-b {
                border-right: none
            }

            .lst {
                -moz-box-sizing: content-box;
                background: #fff;
                color: #000;
                font: 17px arial, sans-serif;
                float: left;
                padding-left: 6px;
                padding-right: 10px;
                vertical-align: top;
                width: 100%
            }

            .lst-td {
                background: #fff;
                border-bottom: 1px solid #999
            }

            .lst-td-xbtn {
                border-top: 1px solid #ccc;
                padding-right: 16px
            }

            .ds {
                border-right: 1px solid #e7e7e7;
                position: relative;
                height: 32px;
                z-index: 100
            }

            .lsbb {
                background: #eee;
                border: 1px solid #999;
                border-top-color: #ccc;
                border-left-color: #ccc;
                height: 30px
            }

            .lsb {
                font: 15px arial, sans-serif;
                background-position: bottom;
                border: none;
                color: #000;
                cursor: pointer;
                height: 30px;
                margin: 0;
                vertical-align: top
            }

            .lsb:active {
                background: #ccc
            }

            .lst:focus {
                outline: none
            }

            .lsd {
                font-size: 11px;
                position: absolute;
                top: 3px;
                left: 16px
            }

            .gl {
                white-space: nowrap
            }

            #searchform {
                position: absolute;
                top: 250px;
                width: 100%;
                z-index: 100
            }

            .tsf-p, .ctr-p > center {
                padding-left: 205px;
                padding-right: 22%
            }

            #search_form, .ctr-p {
                margin: 0 auto;
                max-width: 1181px;
                min-width: 817px
            }

            .fade #center_col, .fade #rhs, .fade #leftnav {
                filter: alpha(opacity = 33.3);
                opacity: 0.333
            }

            #misspell {
                background: transparent;
                color: #fff;
                position: absolute;
                z-index: 2
            }

            .misspelled {
                background: url('http://www.google.com/images/experiments/wavy-underline.png') 50% 100% repeat-x;
                color: #fff;
                display: inline-block;
                line-height: 1.05em;
                padding: 0 0 3px 0
            }

            form {
                display: inline
            }

            input[name="q"] {
                background: transparent;
                position: relative;
                z-index: 3
            }

            #grey {
                background: #fff;
                border-color: transparent;
                color: silver;
                overflow: hidden;
                position: absolute;
                z-index: 1
            }

            #xbtn {
                color: #a1b9ed;
                cursor: pointer;
                display: none;
                font: 28px Arial, sans-serif;
                height: 20px;
                line-height: 28px;
                margin: 4px -8px 4px 0;
                padding: 0;
                top: 2px;
                width: 14px;
                z-index: 4
            }

            #xbtn:hover, .flt, .flt u, a.fl {
                color: #4272db;
                text-decoration: none
            }

            .flt:hover, .flt:hover u, a.fl:hover {
                text-decoration: underline
            }

            .lst {
                border-width: 0 0 0 1px
            }

            #sfopt a:hover {
                text-decoration: none
            }

            #sfopt a.flt:hover {
                text-decoration: underline
            }

            #pocs {
                background: #fff1a8;
                color: #000;
                font-size: 10pt;
                margin: 0;
                padding: 6px 7px
            }

            #pocs.sft {
                background: #fff;
                color: #777
            }

            #pocs a {
                color: #11c
            }

            #pocs.sft a {
                color: #4373db
            }

            #pocs > div {
                margin: 0;
                padding: 0
            }

            #knavm {
                color: #4273db;
                display: inline;
                font: 11px arial, sans-serif !important;
                left: -13px;
                position: absolute;
                top: 2px;
                z-index: 2
            }

            #pnprev #knavm {
                bottom: 1px;
                top: auto
            }

            #pnnext #knavm {
                bottom: 1px;
                left: 40px;
                top: auto
            }

            a.noline {
                outline: 0
            }

            #search_form_div {
                position: absolute;
                top: 250px;
                width: 100%;
                z-index: 100;
            }

            #search_form {
                display:block;
                margin:0 auto;
                background:none;
                width: 600px;
            }

            .invisible_table {
                border: 0;
            }

            .full_cell {
                width: 100%;
            }

            .search_box {
                width: 100%;
                border-bottom: 1px solid #e7e7e7;
                padding: 8px 0 0;
                position: relative
            }

            .search_buttons {
                border-top:1px solid #ccc;
            } </style>
           """