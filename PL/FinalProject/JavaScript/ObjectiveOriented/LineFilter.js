
class LineFilter{
    constructor(searcString){
    this.searchString = searcString;
    }
    process(text){
        let lines = text.split(/\r\n+/);
        //lines = lines.filter(line => line.includes(this.searchString))
        //    .map(line => line+'\n').toString();
        lines = lines.filter(line => line.includes(this.searchString))
            .reduce((text, line) => text.concat(line.concat('\n')), "");
        return lines;
    }
}

module.exports = LineFilter;

