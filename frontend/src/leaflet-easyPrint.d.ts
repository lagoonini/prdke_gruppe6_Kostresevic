import { Control } from 'leaflet';

declare module 'leaflet' {
   interface EasyPrintOptions {
    title?: string;
    position?: string;
    sizeModes?: string[];
    filename?: string;
    exportOnly?: boolean;
    hideControlContainer?: boolean;
    hidden?: boolean;
  }

  interface EasyPrintControl extends Control {
    printMap(sizeMode: string, filename: string): void;
  }

  function easyPrint(options?: EasyPrintOptions): EasyPrintControl;
}
