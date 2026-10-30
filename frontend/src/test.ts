import 'zone.js/testing';

import { getTestBed } from '@angular/core/testing';
import {
  BrowserTestingModule,
  platformBrowserTesting,
} from '@angular/platform-browser/testing';

declare const require: {
  context(
    path: string,
    deep: boolean,
    filter: RegExp,
  ): { keys(): string[]; (id: string): unknown };
};

getTestBed().initTestEnvironment(
  BrowserTestingModule,
  platformBrowserTesting(),
);

// Discover every spec from the test entry point. Karma does not scan for specs
// automatically when a custom main file is configured.
const specs = require.context('./', true, /\.spec\.ts$/);
specs.keys().forEach(specs);
