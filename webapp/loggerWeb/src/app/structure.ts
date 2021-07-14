export interface IStructure {
    id : string,
    name : string,
    parent : string,
    level_leaf : string,
    dtype : string,
    slider_entries : string,
    low_lim : string,
    high_lim : string,
    disable_entry : string,
    hint_text : string,
    default_value : string,
    value : string,
    unit : string,
    edit_enabled : boolean,
    is_hidden : boolean,
    levels_allowed : Array<string>
}