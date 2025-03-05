var val = /%[^\s%]+%/,
    keywords = /^如果尾$|^循环尾$|^返回$/m
    vars_exp = [
        /^\S(?=:)/m,  //单行
        val
    ]
    func = [{
        pattern: /\$\S+/,
        lookbehind: !0,
        greedy: !0
    },
        /(?!\\)\$/
    ]
    json_exp = {
        pattern: /@.(\[[\S]+\])+/,
        inside: {
            // variable: /(?=@)./,
            json_var: {
                pattern: /@./,
                alias: "function"
            },
            elem: {
                pattern: /\[[\S]+\]/,
                inside: {
                    variable: val
                }
            }
        }
    }
    line_exp = {
        function: func,
        variable: vars_exp,
        json:json_exp,
    }
    math_exp={
        pattern: /\[((?<!\d)-?\d+(\.\d+)?|%[^\s%]+%)([+\-*/%()^]((?<!\d)-?\d+(\.\d+)?|%[^\s%]+%))*\]/,
        inside: {
            variable: val,
            operator: /[+\-*/%()^\[\]]/,
            number: /(?<!\d)-?\d+(\.\d+)?/
        }
    }

Prism.languages.nyaplus = {

    "head": {
        greedy: !0,
        pattern: /([\n\r]{2,}).*[\n\r]/,
        inside: Prism.languages.regex
    },

    comment: {
        pattern: /##.*/,
        lookbehind: !0,
        greedy: !0
    },

    function: func,
    json:json_exp,
    mathExp: math_exp,
    variable:vars_exp,
    keyword:keywords,
    end: {    //useless
        pattern: /(?=\s)[\n\r]{2,}/,
        lookbehind:!0,
        greedy:!0
    },
    loopHead: {
        pattern: /^循环:\S+/m,
        greedy:!0,
        inside: {
            divider: /;/,
            compare_op: /(==|>=|<=|!=|<|>)/,
            bool_op:/[&|]/,
            keyword: /循环:/,
            mathExp: math_exp,
            variable: vars_exp,
            function: func,
            number: /(?<!\d)-?\d+(\.\d+)?/,
        }

    },
    ifHead: {
        pattern: /^如果:\S+/m,
        greedy:!0,
        inside: {
            compare_op: /(==|>=|<=|!=|<|>)/,
            bool_op:/[&|]/,
            keyword: /如果:/,
            variable: vars_exp,
            function: func
        }

    },
    number: /(?<!\d)-?\d+(\.\d+)?/,

}